package ru.xunto.roleplaychat.framework.state;

public class Property<T> implements IProperty<T>, Cloneable {
    private final String name;
    private final boolean colorful;

    public Property(String name) {
        this(name, true);
    }

    public Property(String name, boolean colorful) {
        this.name = name;
        this.colorful = colorful;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String stringify(T value) {
        return value.toString();
    }

    public boolean isColorful() {
        return colorful;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
