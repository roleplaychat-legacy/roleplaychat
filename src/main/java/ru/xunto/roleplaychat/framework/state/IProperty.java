package ru.xunto.roleplaychat.framework.state;

public interface IProperty<T> {
    String getName();

    String stringify(T value);

    boolean isColorful();
}
