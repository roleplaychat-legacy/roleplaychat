package ru.xunto.roleplaychat.framework.jtwig.properties;

import java.util.Arrays;

public class JTwigArrayProperty extends JTwigIterableProperty {
    public JTwigArrayProperty(Object[] value) {
        super(Arrays.asList(value));
    }
}
