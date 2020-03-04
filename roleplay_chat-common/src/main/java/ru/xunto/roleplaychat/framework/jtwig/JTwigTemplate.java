package ru.xunto.roleplaychat.framework.jtwig;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.Map;

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

    public static String wrapWithColor(String value, String colorName) {
        return color(colorName) + value + color("default");
    }

    public static String color(String colorName) {
        return COLOR_MARKER + colorName + COLOR_MARKER;
    }

    public static String escape(String text) {
        return text.replace(COLOR_MARKER, COLOR_MARKER_ESCAPED);
    }

    public static String unescape(String escapedText) {
        return escapedText.replace(COLOR_MARKER_ESCAPED, COLOR_MARKER);
    }

    private Text renderText(String content, Map<String, TextColor> colors) {
        TextColor defaultColor = colors.getOrDefault("default", TextColor.WHITE);

        Text text = new Text(defaultColor);

        TextColor color = defaultColor;
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

            TextComponent component = this.coloredComponent(unescape(before), color);
            text.add(component);

            color = colors.getOrDefault(colorName, defaultColor);
        }

        return text;
    }

    @Override
    public Text render(JTwigState state, Map<String, TextColor> colors) {
        JtwigModel model = state.getModel();
        String content = template.render(model);

        return this.renderText(content, colors);
    }

    private TextComponent coloredComponent(String content, TextColor color) {
        return new TextComponent(content, color);
    }
}
