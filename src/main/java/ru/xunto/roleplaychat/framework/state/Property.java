package ru.xunto.roleplaychat.framework.state;

public class Property<T> implements IProperty<T>, Cloneable {
    private String name;

    public Property(String name) {
        this.name = name;
    }

    @Override public String getName() {
        return name;
    }

    @Override public String stringify(T value) {
        return value.toString();
    }

    @Override protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
