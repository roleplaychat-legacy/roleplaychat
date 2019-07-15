package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.framework.MiddlewareCallback;

public abstract class Endpoint extends Middleware {
    @Override public Stage getStage() {
        return Stage.ENDPOINT;
    }

    @Override
    public void process(Request request, Environment environment, MiddlewareCallback next) {
        this.processWrapped(request, environment, next);
        next.call();
    }

    public void processWrapped(Request request, Environment environment, MiddlewareCallback next) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.preProcessEndpoint(request, environment, next);
        this.processEndpoint(request, environment, next);
        environment.setProcessed(true);
        this.postProcessEndpoint(request, environment, next);
    }

    public abstract boolean matchEndpoint(Request request, Environment environment);

    public void preProcessEndpoint(Request request, Environment environment,
        MiddlewareCallback next) {
    }

    public void processEndpoint(Request request, Environment environment, MiddlewareCallback next) {
        this.processEndpoint(request, environment);
    }

    public void postProcessEndpoint(Request request, Environment environment,
        MiddlewareCallback next) {
    }

    public void processEndpoint(Request request, Environment environment) {

    }
}
