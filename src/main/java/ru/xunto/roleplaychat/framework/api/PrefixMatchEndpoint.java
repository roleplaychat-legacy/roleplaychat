package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

public abstract class PrefixMatchEndpoint extends Endpoint {
    public final static IProperty<PrefixMatchEndpoint> FORCED_ENDPOINT = new Property<>("endpoint");

    private final String[] prefixes;

    public PrefixMatchEndpoint(String... prefixes) throws EmptyPrefixException {
        if (prefixes.length == 0)
            throw new EmptyPrefixException();
        this.prefixes = prefixes;
    }

    public String[] getPrefixes() {
        return prefixes;
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        boolean forced = environment.getState().getValue(FORCED_ENDPOINT) == this;
        if (forced) return true;

        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                text = text.replaceFirst(prefix, "").trim();
                state.setValue(Environment.TEXT, text);
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public class EmptyPrefixException extends Exception {
    }
}
