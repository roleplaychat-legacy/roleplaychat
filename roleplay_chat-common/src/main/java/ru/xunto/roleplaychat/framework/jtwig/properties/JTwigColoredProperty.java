package ru.xunto.roleplaychat.framework.jtwig.properties;

import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.state.IProperty;

public class JTwigColoredProperty extends JTwigEscapedProperty {
    protected final IProperty property;

    public JTwigColoredProperty(IProperty property, Object value) {
        super(value);
        this.property = property;
    }

    @Override
    public String toString() {
        return JTwigTemplate.wrapWithColor(super.toString(), this.property.getName());
    }
}
