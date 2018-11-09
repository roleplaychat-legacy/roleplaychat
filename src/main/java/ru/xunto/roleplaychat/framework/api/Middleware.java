package ru.xunto.roleplaychat.framework.api;

public abstract class Middleware {
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public abstract Stage getStage();

    public abstract void process(Request request, Environment environment);
}
