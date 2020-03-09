package ru.xunto.roleplaychat.framework.middleware_flow;

import ru.xunto.roleplaychat.features.middleware.distance.ListenMiddleware;
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

    /**
     * Light fork creates new flow which doesn't count as separate flow (as opposed to hard fork).
     * It is mostly used to send the same message with some minor alterations to some recipients.
     * <p>
     * It doesn't have separate log entry end treated as single message by external modules.
     *
     * @param environment message generation environment for new Flow.
     * @see ListenMiddleware for examples
     */
    public void lightFork(Environment environment) {
        Flow newFlow = new Flow(middlewareQueue, request, environment, endCallback);
        environment.getState().setValue(IS_LIGHT_FORK, true);
        newFlow.next();
    }

    /**
     * Pass processing to next middleware.
     */
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

    /**
     * Fork (or hard fork) creates new flow. It is used to create new different message with it's own
     * recipients but based on the same flow and request.
     * <p>
     * Example: Message that contains translation of fantasy language (based on permission) would be a hard work.
     *
     * @param environment message generation environment for new Flow.
     */
    public void fork(Environment environment) {
        Flow newFlow = new Flow(middlewareQueue, request, environment, endCallback);
        newFlow.next();
    }

    /**
     * Stop processing usually used when there was expected error when processing endpoint.
     */
    public void stop() {
        stopped = true;
    }
}
