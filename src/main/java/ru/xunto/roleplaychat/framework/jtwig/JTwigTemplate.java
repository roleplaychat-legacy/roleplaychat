package ru.xunto.roleplaychat.framework.jtwig;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;

import java.util.HashMap;
import java.util.Map;

import static ru.xunto.roleplaychat.features.endpoints.ActionEndpoint.ACTION_PARTS;
import static ru.xunto.roleplaychat.features.endpoints.ActionEndpoint.START_WITH_ACTION;

public class JTwigTemplate implements ITemplate<JTwigState> {
    private final static String COLOR_MARKER = "$";
    private final static String COLOR_MARKER_ESCAPED = "\16";
    private final static EnvironmentConfiguration CONF =
        EnvironmentConfigurationBuilder.configuration().functions().add(new WrapColorFunction())
            .and().build();
    private JtwigTemplate template;

    public JTwigTemplate(String path) {
        this.template = JtwigTemplate.classpathTemplate(path, CONF);
    }

    public static String color(String colorName) {
        return COLOR_MARKER + colorName + COLOR_MARKER;
    }

    public static String wrapWithColor(String value, String colorName) {
        return color(colorName) + value + color("default");
    }

    public static String escape(String text) {
        return text.replace(COLOR_MARKER, COLOR_MARKER_ESCAPED);
    }

    public static String unescape(String escapedText) {
        return escapedText.replace(COLOR_MARKER_ESCAPED, COLOR_MARKER);
    }

    private ITextComponent coloredComponent(String content, TextFormatting color) {
        ITextComponent component = new TextComponentString(content);
        component.getStyle().setColor(color);
        return component;
    }

    private ITextComponent renderText(String content, Map<String, TextFormatting> colors) {
        TextFormatting defaultColor = colors.getOrDefault("default", TextFormatting.WHITE);

        ITextComponent components = this.coloredComponent("", defaultColor);

        TextFormatting color = defaultColor;
        String left = content;
        while (!left.isEmpty()) {
            int begin = left.indexOf(COLOR_MARKER);
            int end = left.indexOf(COLOR_MARKER, begin + 1);

            String before, colorName;
            if (begin >= 0 && end > 0) {
                before = left.substring(0, begin);
                colorName = left.substring(begin + 1, end);
                left = left.substring(end + 1);
            } else {
                before = left;
                colorName = "default";
                left = "";
            }

            ITextComponent component = this.coloredComponent(unescape(before), color);
            component.getStyle().setColor(color);
            components.appendSibling(component);

            color = colors.getOrDefault(colorName, defaultColor);
        }

        return components;
    }

    @Override public ITextComponent render(JTwigState state, Map<String, TextFormatting> colors) {
        JtwigModel model = state.getModel();
        String content = template.render(model);

        return this.renderText(content, colors);
    }

    public static void testCase(ITemplate template, JTwigState state, String expectedResult) {
        Map<String, TextFormatting> colors = new HashMap<>();
        String result = template.render(state, colors).getUnformattedText();

        if (!result.equals(expectedResult)) {
            System.out.println(String.format("%s != %s", result, expectedResult));
            System.exit(1);
        }

        System.out.println(String.format("Result (%s): [OK]", result));
    }


    public static JTwigState setUpState() {
        JTwigState state = new JTwigState();
        state.setValue(Environment.USERNAME, "username");
        state.setValue(Environment.TEXT, "text");
        return state;
    }

    public static void main(String args[]) {

        ITemplate template;
        JTwigState state;

        // Default
        System.out.println("=== Default ===");
        state = setUpState();
        template = new JTwigTemplate("templates/default.twig");
        testCase(template, state, "username: text");
        state.setValue(Environment.LABEL, "label");
        testCase(template, state, "username (label): text");

        System.out.println("=== GM OOC ===");
        state = setUpState();
        template = new JTwigTemplate("templates/gm_ooc.twig");
        testCase(template, state, "username (To GM): (( text ))");

        System.out.println("=== OOC ===");
        state = setUpState();
        template = new JTwigTemplate("templates/ooc.twig");
        testCase(template, state, "username (OOC): (( text ))");
        state.setValue(Environment.LABEL, "label");
        testCase(template, state, "username (label): (( text ))");

        state = setUpState();
        template = new JTwigTemplate("templates/action.twig");
        state.setValue(ACTION_PARTS, new String[] {"text1", "text2", "text3", "text4"});

        // Action without label
        System.out.println("=== Action without label ===");
        state.setValue(START_WITH_ACTION, false);
        testCase(template, state, "username: text1 * text2 * text3 * text4 *");
        state.setValue(START_WITH_ACTION, true);
        testCase(template, state, "* username text1 * text2 * text3 * text4");

        // Action with label
        System.out.println("=== Action with label ===");

        state.setValue(Environment.LABEL, "label");
        state.setValue(START_WITH_ACTION, false);
        testCase(template, state, "username (label): text1 * text2 * text3 * text4 *");
        state.setValue(START_WITH_ACTION, true);
        testCase(template, state, "* username (label) text1 * text2 * text3 * text4");

    }
}
