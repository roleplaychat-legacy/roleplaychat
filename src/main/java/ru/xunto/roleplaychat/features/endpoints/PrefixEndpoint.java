package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.MessageState;

public abstract class PrefixEndpoint extends Endpoint {
    private final String[] prefixes;

    PrefixEndpoint(String... prefixes) {
        this.prefixes = prefixes;
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Core.TEXT);

        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                text = text.substring(1).trim();
                state.setValue(Core.TEXT, text);
                return true;
            }
        }

        return false;
    }
}
