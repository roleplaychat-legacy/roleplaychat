package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.commands.CommandEndpoint;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.Arrays;
import java.util.Comparator;

public abstract class PrefixMatchEndpoint extends Endpoint {
    public final static IProperty<PrefixMatchEndpoint> FORCED_ENDPOINT = new Property<>("endpoint");

    private final String[] prefixes;
    private RoleplayChatCore core;

    public PrefixMatchEndpoint(RoleplayChatCore core, String... prefixes) throws EmptyPrefixError {
        this.core = core;
        if (prefixes.length == 0)
            throw new EmptyPrefixError();

        Arrays.sort(prefixes, Comparator.comparingInt(String::length).reversed());
        this.prefixes = prefixes;
    }

    public String[] getPrefixes() {
        return prefixes;
    }

    @Override
    public boolean matchEndpoint(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        boolean forced = environment.getState().getValue(FORCED_ENDPOINT) == this;
        if (forced)
            return true;

        for (String prefix : prefixes) {
            if (text.startsWith(prefix))
                return true;
        }

        return false;
    }

    @Override
    public void preProcessEndpoint(Request request, Environment environment) {
        removePrefix(environment);
    }

    public void removePrefix(Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                text = text.replaceFirst(prefix, "").trim();
                state.setValue(Environment.TEXT, text);
            }
        }
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public boolean canSay(ISpeaker speaker) {
        return true;
    }

    public PrefixMatchEndpoint registerCommand(String name) {
        this.core.register(new CommandEndpoint<>(this.core, name, this));
        return this;
    }

    public static class EmptyPrefixError extends Error {
    }
}
