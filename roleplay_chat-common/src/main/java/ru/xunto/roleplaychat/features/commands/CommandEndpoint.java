package ru.xunto.roleplaychat.features.commands;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.commands.CommandException;
import ru.xunto.roleplaychat.framework.state.MessageState;

public class CommandEndpoint<T extends PrefixMatchEndpoint> implements ICommand {
    private final String name;
    private RoleplayChatCore core;
    private T endpoint;

    public CommandEndpoint(RoleplayChatCore core, String name, T endpoint) {
        this.core = core;
        this.name = name;
        this.endpoint = endpoint;
    }

    @Override
    public String getCommandName() {
        return this.name;
    }

    @Override
    public String[] getTabCompletion(ISpeaker speaker, String[] args) {
        return new String[0];
    }

    @Override
    public boolean canExecute(ISpeaker speaker) {
        return endpoint.canSay(speaker);
    }

    @Override
    public void execute(ISpeaker speaker, String[] args) throws CommandException {
        String msg = String.join(" ", args);
        Request request = new Request(msg, speaker);
        Environment environment = new Environment(speaker.getName(), msg);
        MessageState state = environment.getState();
        state.setValue(PrefixMatchEndpoint.FORCED_ENDPOINT, endpoint);
        core.process(request, environment);
    }
}
