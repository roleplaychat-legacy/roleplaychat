package ru.xunto.roleplaychat.framework.api;

public abstract class Endpoint extends Middleware {
    public abstract boolean matchEndpoint(Request request, Environment environment);

    public abstract void processEndpoint(Request request, Environment environment);

    @Override public Stage getStage() {
        return Stage.ENDPOINT;
    }

    public void preProcessEndpoint(Request request, Environment environment) {
    }

    ;

    @Override public void process(Request request, Environment environment) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.preProcessEndpoint(request, environment);
        this.processEndpoint(request, environment);
        environment.setProcessed(true);
    }
}
