package ru.xunto.roleplaychat.framework.jtwig;

import org.jtwig.JtwigModel;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigArrayProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigColoredProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigEscapedProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigIterableProperty;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;

public class JTwigState extends MessageState {
    private JtwigModel model = new JtwigModel();

    public static Object getProperty(IProperty property, Object value) {
        if (value instanceof String) {
            if (property != null && property.isColorful()) {
                return new JTwigColoredProperty(property, value);
            } else {
                return new JTwigEscapedProperty(value);
            }
        }

        if (value instanceof Iterable) {
            return new JTwigIterableProperty((Iterable<Object>) value);
        }

        if (value instanceof Object[]) {
            return new JTwigArrayProperty((Object[]) value);
        }

        return value;
    }

    @Override public <E> void setValue(IProperty<E> property, E value) {
        model = model.with(property.getName(), JTwigState.getProperty(property, value));
        super.setValue(property, value);
    }

    @Override public JTwigState clone() throws CloneNotSupportedException {
        return (JTwigState) super.clone();
    }

    public JtwigModel getModel() {
        return model;
    }
}
