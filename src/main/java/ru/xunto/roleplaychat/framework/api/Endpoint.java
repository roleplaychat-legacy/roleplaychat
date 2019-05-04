package ru.xunto.roleplaychat.framework.api;

public abstract class Endpoint extends Middleware {
    public abstract boolean matchEndpoint(Request request, Environment environment);

    public abstract void processEndpoint(Request request, Environment environment) throws ChatException;

    @Override public Stage getStage() {
        return Stage.ENDPOINT;
    }

    @Override public void process(Request request, Environment environment) throws ChatException {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.processEndpoint(request, environment);
        environment.setProcessed(true);
    }
}
