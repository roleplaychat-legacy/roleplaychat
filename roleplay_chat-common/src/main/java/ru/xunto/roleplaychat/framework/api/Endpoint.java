package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

public abstract class Endpoint extends Middleware {
    @Override
    public Stage getStage() {
        return Stage.ENDPOINT;
    }

    @Override
    public void process(Request request, Environment environment, Flow flow) {
        this.processWrapped(request, environment, flow);
        flow.next();
    }

    public void processWrapped(Request request, Environment environment, Flow flow) {
        if (environment.isProcessed())
            return;

        if (!this.matchEndpoint(request, environment))
            return;

        this.preProcessEndpoint(request, environment);
        this.processEndpoint(request, environment, flow);
        environment.setProcessed(true);
        this.postProcessEndpoint(request, environment);
    }

    public abstract boolean matchEndpoint(Request request, Environment environment);

    public void preProcessEndpoint(Request request, Environment environment) {
    }

    public void processEndpoint(Request request, Environment environment, Flow flow) {
        this.processEndpoint(request, environment);
    }

    public void postProcessEndpoint(Request request, Environment environment) {
    }

    public void processEndpoint(Request request, Environment environment) {

    }
}
