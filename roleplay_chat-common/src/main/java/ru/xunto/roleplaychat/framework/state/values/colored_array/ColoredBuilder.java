package ru.xunto.roleplaychat.framework.state.values.colored_array;

import java.util.ArrayList;
import java.util.List;

public class ColoredBuilder {
    private static String DEFAULT = "default";

    private String marker = DEFAULT;
    private StringBuilder builder = new StringBuilder();

    private List<ColoredText> parts = new ArrayList<>();

    public ColoredBuilder add(String content) {
        return this.add(content, DEFAULT);
    }

    private void finishPart() {
        this.parts.add(new ColoredText(builder.toString(), this.marker));
        this.builder = new StringBuilder();
    }

    public ColoredBuilder add(String content, String marker) {
        if (!marker.equals(this.marker)) {
            this.finishPart();
            this.marker = marker;
        }

        builder.append(content);
        return this;
    }

    public List<ColoredText> build() {
        this.finishPart();
        return parts;
    }
}
