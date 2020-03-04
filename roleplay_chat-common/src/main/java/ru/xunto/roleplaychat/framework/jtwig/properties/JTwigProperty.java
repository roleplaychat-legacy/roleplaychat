package ru.xunto.roleplaychat.framework.jtwig.properties;

public abstract class JTwigProperty<E> {
    protected final E value;

    public JTwigProperty(E value) {
        this.value = value;
    }

    @Override public String toString() {
        return value.toString();
    }
}
