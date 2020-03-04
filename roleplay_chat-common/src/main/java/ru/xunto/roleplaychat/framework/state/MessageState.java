package ru.xunto.roleplaychat.framework.state;

import java.util.HashMap;
import java.util.Map;

public class MessageState implements Cloneable {
    private Map<String, IProperty> properties = new HashMap<>();
    private Map<IProperty, Object> state = new HashMap<>();

    public <E> E getValue(IProperty<E> property) {
        return getValue(property, null);
    }

    public <E> E getValue(IProperty<E> property, E defaultValue) {
        Object value = state.getOrDefault(property, defaultValue);

        if (value == null)
            return null;

        return (E) value;
    }

    public String getValue(String name) {
        if (!properties.containsKey(name))
            return null;
        IProperty property = properties.get(name);
        if (!state.containsKey(property))
            return null;
        return property.stringify(state.get(property));
    }

    public <E> void setValue(IProperty<E> property, E value) {
        properties.put(property.getName(), property);
        state.put(property, value);
    }

    @Override public MessageState clone() throws CloneNotSupportedException {
        MessageState clone = (MessageState) super.clone();
        clone.properties = new HashMap<>(properties);
        clone.state = new HashMap<>(state);
        return clone;
    }
}
