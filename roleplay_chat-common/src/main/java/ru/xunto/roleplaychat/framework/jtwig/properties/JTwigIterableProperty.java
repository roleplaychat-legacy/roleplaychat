package ru.xunto.roleplaychat.framework.jtwig.properties;

import ru.xunto.roleplaychat.framework.jtwig.JTwigState;

import java.util.ArrayList;
import java.util.Iterator;

public class JTwigIterableProperty extends JTwigProperty<Iterable> implements Iterable {
    public JTwigIterableProperty(Iterable<Object> value) {
        super(value);
    }

    @Override
    public Iterator iterator() {
        ArrayList<Object> objects = new ArrayList<>();

        for (Object o : this.value) {
            objects.add(JTwigState.getProperty(null, o));
        }

        return objects.iterator();
    }
}
