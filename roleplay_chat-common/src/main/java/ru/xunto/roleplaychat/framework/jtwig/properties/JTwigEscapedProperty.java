package ru.xunto.roleplaychat.framework.jtwig.properties;

import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

public class JTwigEscapedProperty extends JTwigProperty {

    public JTwigEscapedProperty(Object value) {
        super(value);
    }

    @Override
    public String toString() {
        return JTwigTemplate.escape(super.toString());
    }
}
