package ru.xunto.roleplaychat.framework.api;

public abstract class Endpoint extends Middleware {
    public abstract boolean matchEndpoint(Request request, Environment environment);

    public abstract void processEndpoint(Environment environment);

    @Override public Stage getStage() {
        return Stage.ENDPOINT;
    }

    @Override public void process(Request request, Environment environment) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.processEndpoint(environment);
        environment.setProcessed(true);
    }
}
