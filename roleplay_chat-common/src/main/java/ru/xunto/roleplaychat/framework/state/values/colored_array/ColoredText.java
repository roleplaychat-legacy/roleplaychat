package ru.xunto.roleplaychat.framework.state.values.colored_array;

public class ColoredText {
    private final String content;
    private final String marker;

    public ColoredText(String content, String marker) {
        this.content = content;
        this.marker = marker;
    }

    public String getContent() {
        return content;
    }

    public String getMarker() {
        return marker;
    }

    @Override
    public String toString() {
        return "TextPart{" +
                "content='" + content + '\'' +
                ", marker='" + marker + '\'' +
                '}';
    }
}
