package ru.xunto.roleplaychat.framework.api;

public abstract class Endpoint extends Middleware {
    public abstract boolean matchEndpoint(Request request, Environment environment);

    @Override public Stage getStage() {
        return Stage.ENDPOINT;
    }

    public void preProcessEndpoint(Request request, Environment environment, Runnable next) {
    }

    public void postProcessEndpoint(Request request, Environment environment, Runnable next) {
    }

    public void processEndpoint(Request request, Environment environment) {

    }

    public void processEndpoint(Request request, Environment environment, Runnable next) {
        this.processEndpoint(request, environment);
    }

    public void processWrapped(Request request, Environment environment, Runnable next) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.preProcessEndpoint(request, environment, next);
        this.processEndpoint(request, environment, next);
        environment.setProcessed(true);
        this.postProcessEndpoint(request, environment, next);
    }

    @Override public void process(Request request, Environment environment, Runnable next) {
        this.processWrapped(request, environment, next);
        next.run();
    }
}
