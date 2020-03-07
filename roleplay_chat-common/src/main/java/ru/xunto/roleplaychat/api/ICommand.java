package ru.xunto.roleplaychat.api;

import ru.xunto.roleplaychat.framework.commands.CommandException;

public interface ICommand {
    String getCommandName();

    String[] getTabCompletion(ISpeaker speaker, String[] args);

    boolean canExecute(ISpeaker speaker);

    void execute(ISpeaker speaker, String[] args) throws CommandException;
}
