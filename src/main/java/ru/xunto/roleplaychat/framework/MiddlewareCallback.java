package ru.xunto.roleplaychat.framework;

import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class MiddlewareCallback {
    private final Request request;
    private final Environment environment;
    private final Consumer<Environment> endCallback;
    private Queue<Middleware> middlewareQueue;

    public MiddlewareCallback(Collection<Middleware> middleware, Request request,
        Environment environment, Consumer<Environment> endCallback) {
        middlewareQueue = new LinkedList<>(middleware);
        this.request = request;
        this.environment = environment;
        this.endCallback = endCallback;
    }

    public void call(Environment environment) {
        MiddlewareCallback clone = this.clone(request, environment);
        clone.call();
    }

    public MiddlewareCallback clone(Request request, Environment environment) {
        return new MiddlewareCallback(middlewareQueue, request, environment, endCallback);
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
