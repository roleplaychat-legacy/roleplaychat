package ru.xunto.roleplaychat.framework.middleware_flow;

import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class Flow {
    public static IProperty<Boolean> IS_LIGHT_FORK = new Property<>("is_light_fork");

    private final Request request;
    private final Environment environment;
    private final Consumer<Environment> endCallback;
    private Queue<Middleware> middlewareQueue;

    private boolean stopped = false;

    public Flow(Collection<Middleware> middleware, Request request, Environment environment,
        Consumer<Environment> endCallback) {
        middlewareQueue = new LinkedList<>(middleware);
        this.request = request;
        this.environment = environment;
        this.endCallback = endCallback;
    }

    public void lightFork(Environment environment) {
        Flow newFlow = new Flow(middlewareQueue, request, environment, endCallback);
        environment.getState().setValue(IS_LIGHT_FORK, true);
        newFlow.next();
    }

    public void next() {
        if (stopped)
            return;

        Middleware nextMiddleware = middlewareQueue.poll();

        if (nextMiddleware == null) {
            endCallback.accept(environment);
            return;
        }

        nextMiddleware.process(request, environment, this);
    }

    public void fork(Environment environment) {
        Flow newFlow = new Flow(middlewareQueue, request, environment, endCallback);
        newFlow.next();
    }

    public void stop() {
        stopped = true;
    }
}
