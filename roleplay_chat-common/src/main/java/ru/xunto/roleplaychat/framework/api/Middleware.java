package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

public abstract class Middleware {
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public abstract Stage getStage();

    public abstract void process(Request request, Environment environment, Flow flow);
}
