package ru.xunto.roleplaychat.framework.pebble;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PebbleChatTemplate implements ITemplate {
    public final static PebbleEngine engine = new PebbleEngine.Builder()
            .extension(new RoleplayChatExtension()).build();
    private final static String COLOR_MARKER = "$";
    private final static String COLOR_MARKER_ESCAPED = "\16";
    PebbleTemplate compiledTemplate;

    public PebbleChatTemplate(String templatePath) {
        this.compiledTemplate = PebbleChatTemplate.engine.getTemplate(templatePath);
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

    private TextComponent coloredComponent(String content, TextColor color) {
        return new TextComponent(content, color);
    }

    @Override
    public Text render(MessageState state, Map<String, TextColor> colors) {
        Writer writer = new StringWriter();

        try {
            compiledTemplate.evaluate(writer, state.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.renderText(writer.toString(), colors);
    }
}
