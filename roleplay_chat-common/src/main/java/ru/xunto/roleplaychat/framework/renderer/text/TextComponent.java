package ru.xunto.roleplaychat.framework.renderer.text;

public class TextComponent {
    private final String content;
    private final TextColor color;
    private Text hoverText = null;

    public TextComponent(String content, TextColor color) {
        this.content = content;
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public TextColor getColor() {
        return color;
    }

    public Text getHoverText() {
        return hoverText;
    }

    public void setHoverText(Text hoverText) {
        this.hoverText = hoverText;
    }
}
