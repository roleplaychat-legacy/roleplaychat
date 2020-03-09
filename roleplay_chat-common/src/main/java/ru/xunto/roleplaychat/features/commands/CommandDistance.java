package ru.xunto.roleplaychat.features.commands;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.middleware.distance.Distance;
import ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.commands.CommandException;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;

public class CommandDistance<T extends Distance> implements ICommand {
    private final String name;
    private RoleplayChatCore core;
    private T distance;

    public CommandDistance(RoleplayChatCore core, String name, T distance) {
        this.core = core;
        this.name = name;
        this.distance = distance;
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
        return true;
    }

    @Override
    public void execute(ISpeaker speaker, String[] args) throws CommandException {
        String msg = String.join(" ", args);
        Request request = new Request(msg, speaker);
        Environment environment = new Environment(speaker.getName(), msg);

        JTwigState state = environment.getState();
        state.setValue(DistanceMiddleware.FORCE_ENVIRONMENT, true);
        state.setValue(DistanceMiddleware.DISTANCE, distance);

        core.process(request, environment);
    }
}
