package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.MessageState;

public abstract class PrefixMatchEndpoint extends Endpoint {
    private final String[] prefixes;

    PrefixMatchEndpoint(String... prefixes) throws EmptyPrefixException {
        if (prefixes.length == 0)
            throw new EmptyPrefixException();
        this.prefixes = prefixes;
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                text = text.substring(1).trim();
                state.setValue(Environment.TEXT, text);
                return true;
            }
        }

        return false;
    }

    public class EmptyPrefixException extends Exception {
    }
}
