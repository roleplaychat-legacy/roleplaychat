package ru.xunto.roleplaychat.framework.renderer.text;

import java.util.ArrayList;
import java.util.List;

public class Text {
    private TextColor defaultColor;
    private List<TextComponent> components = new ArrayList<>();
    private Object cache = null;

    public Text(TextColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void add(TextComponent component) {
        this.components.add(component);
    }

    public List<TextComponent> getComponents() {
        return components;
    }

    public TextColor getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(TextColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Object getCache() {
        return this.cache;
    }

    public void setCache(Object cache) {
        this.cache = cache;
    }

    public String getUnformattedText() {
        StringBuilder builder = new StringBuilder();
        for (TextComponent component : components) {
            builder.append(component.getContent());
        }

        return builder.toString();
    }
}
