package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.framework.MiddlewareCallback;

public abstract class Middleware {
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public abstract Stage getStage();

    public abstract void process(Request request, Environment environment, MiddlewareCallback next);
}
