package ru.xunto.roleplaychat.framework.state;

import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;

import java.util.HashMap;
import java.util.Map;

public class MessageState implements Cloneable {
    private Map<String, IProperty<Object>> properties = new HashMap<>();
    private Map<IProperty<Object>, Object> state = new HashMap<>();

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
        IProperty<Object> property = properties.get(name);
        if (!state.containsKey(property))
            return null;
        return property.stringify(state.get(property));
    }

    public <E> void setValue(IProperty<E> property, E value) {
        properties.put(property.getName(), (IProperty<Object>) property);
        state.put((IProperty<Object>) property, value);
    }

    @Override
    public MessageState clone() throws CloneNotSupportedException {
        MessageState clone = (MessageState) super.clone();
        clone.properties = new HashMap<>(properties);
        clone.state = new HashMap<>(state);
        return clone;
    }

    public Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<>();

        for (Map.Entry<IProperty<Object>, Object> entry : state.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof String) {
                value = PebbleChatTemplate.wrapWithColor(value.toString(), entry.getKey().getName());
            }

            context.put(entry.getKey().getName(), value);
        }

        return context;
    }
}
