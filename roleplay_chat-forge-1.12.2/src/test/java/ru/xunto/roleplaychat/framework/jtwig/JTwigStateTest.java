package ru.xunto.roleplaychat.framework.jtwig;

import org.testng.annotations.Test;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.Property;

import static org.testng.Assert.assertEquals;

public class JTwigStateTest {
    public final static IProperty<String> TEXT = new Property<>("text");

    @Test public void testClone() throws CloneNotSupportedException {
        JTwigState state = new JTwigState();

        JTwigState clone = state.clone();
        clone.setValue(TEXT, "test2");
        state.setValue(TEXT, "test1");

        assertEquals(state.getValue(TEXT), "test1");
        assertEquals(clone.getValue(TEXT), "test2");
    }
}
