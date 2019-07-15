package ru.xunto.roleplaychat.framework.middleware_flow;

import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class Flow implements IFork {
    private final Request request;
    private final Environment environment;
    private final Consumer<Environment> endCallback;
    private Queue<Middleware> middlewareQueue;

    public Flow(Collection<Middleware> middleware, Request request, Environment environment,
        Consumer<Environment> endCallback) {
        middlewareQueue = new LinkedList<>(middleware);
        this.request = request;
        this.environment = environment;
        this.endCallback = endCallback;
    }

    @Override public void fork(Environment environment) {
        Flow newFlow = new Flow(middlewareQueue, request, environment, endCallback);
        newFlow.call();
    }

    public void call() {
        Middleware nextMiddleware = middlewareQueue.poll();

        if (nextMiddleware == null) {
            endCallback.accept(environment);
            return;
        }

        nextMiddleware.process(request, environment, this);
    }
}