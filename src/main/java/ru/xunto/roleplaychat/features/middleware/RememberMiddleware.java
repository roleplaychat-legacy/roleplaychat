package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.Distance;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;

import java.util.HashMap;

import static ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.DISTANCE;
import static ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint.FORCED_ENDPOINT;

public class RememberMiddleware extends Middleware {
    HashMap<EntityPlayer, Distance> ranges = new HashMap<>();
    HashMap<EntityPlayer, Class<? extends PrefixMatchEndpoint>>
        endpoints = new HashMap<EntityPlayer, Class<? extends PrefixMatchEndpoint>>();

    @Override public Stage getStage() {
        return Stage.PRE;
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
    }

    private static boolean isSetRequest(String text, String[] prefixes) {
        boolean controls = false;
        for (String prefix : prefixes) {
            controls |= text.startsWith(prefix);
        }

        for (String prefix : prefixes) {
            text = text.replace(prefix, "");
        }

        return controls && text.isEmpty();
    }

    private Distance processRecallDistance(Request request, Environment environment) throws ChatException {
        if (isSetRequest(request.getText(), new String[] {"!", "="})) {
            Distance distance = DistanceMiddleware.processDistanceState(request, environment);
            ranges.put(request.getRequester(), distance);

            throw new ChatException("Set distance: " + distance.toString());
        }

        return ranges.getOrDefault(request.getRequester(), null);
    }

    private Class<? extends PrefixMatchEndpoint> getForcedEndpoint(Request request, Environment environment) {
        for (Middleware middleware : environment.getCore().getMiddleware()) {
            if (middleware instanceof PrefixMatchEndpoint) {
                PrefixMatchEndpoint endpoint = (PrefixMatchEndpoint) middleware;
                if (isSetRequest(request.getText(), endpoint.getPrefixes())) {
                    return endpoint.getClass();
                }
            }
        }

        return null;
    }

    private Class<? extends PrefixMatchEndpoint> processRecallEndpoint(Request request, Environment environment) throws ChatException {
        Class<? extends PrefixMatchEndpoint> endpoint = getForcedEndpoint(request, environment);

        if (endpoint != null) {
            endpoints.put(request.getRequester(), endpoint);
            throw new ChatException("Set endpoint: " + endpoint.toString());
        }

        endpoint = endpoints.getOrDefault(request.getRequester(), null);

        return endpoint;
    }

    @Override public void process(Request request, Environment environment) throws ChatException {
        JTwigState state = environment.getState();

        Distance distance = processRecallDistance(request, environment);

        if (distance != null) {
            state.setValue(DISTANCE, distance);
        }

        Class<? extends PrefixMatchEndpoint> endpoint = processRecallEndpoint(request, environment);

        if (endpoint != null) {
            state.setValue(FORCED_ENDPOINT, endpoint);
        }
    }
}
