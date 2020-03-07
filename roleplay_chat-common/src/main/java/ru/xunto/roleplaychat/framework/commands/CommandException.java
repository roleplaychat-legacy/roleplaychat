package ru.xunto.roleplaychat.framework.commands;

public class CommandException extends Exception {
    public CommandException(String errorMessage) {
        super(errorMessage);
    }
}
