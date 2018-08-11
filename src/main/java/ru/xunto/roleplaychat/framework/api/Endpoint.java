package ru.xunto.roleplaychat.framework.api;

public abstract class Endpoint extends Middleware {
    public abstract boolean matchEndpoint(Environment environment);

    public abstract void processEndpoint(Environment environment);

    @Override public Priority getPriority() {
        return Priority.LOW;
    }

    @Override public void process(Request request, Environment environment) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(environment))
            return;

        this.processEndpoint(environment);
        environment.setProcessed(true);
    }
}
