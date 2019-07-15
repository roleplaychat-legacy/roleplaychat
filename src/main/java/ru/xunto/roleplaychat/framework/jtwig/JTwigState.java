package ru.xunto.roleplaychat.framework.jtwig;

import org.jtwig.JtwigModel;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigArrayProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigColoredProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigEscapedProperty;
import ru.xunto.roleplaychat.framework.jtwig.properties.JTwigIterableProperty;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.HashMap;
import java.util.Map;

public class JTwigState extends MessageState {
    private JtwigModel model = new JtwigModel();
    private Map<String, Object> jTwigState = new HashMap<>();

    @Override public <E> void setValue(IProperty<E> property, E value) {
        Object jTwigValue = JTwigState.getProperty(property, value);
        model = model.with(property.getName(), jTwigValue);
        jTwigState.put(property.getName(), jTwigValue);
        super.setValue(property, value);
    }

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

    @Override public JTwigState clone() throws CloneNotSupportedException {
        JTwigState clone = (JTwigState) super.clone();
        clone.jTwigState = new HashMap<>(jTwigState);
        clone.model = JtwigModel.newModel(jTwigState);
        return clone;
    }

    public JtwigModel getModel() {
        return model;
    }
}
