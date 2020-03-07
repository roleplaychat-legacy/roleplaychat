package ru.xunto.roleplaychat.features.middleware.remember;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

import java.util.HashMap;
import java.util.UUID;

import static ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint.FORCED_ENDPOINT;

public class RecallEndpointMiddleware extends AbstractRecallMiddleware {
    private HashMap<UUID, PrefixMatchEndpoint> endpoints = new HashMap<>();

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public Stage getStage() {
        return Stage.PRE;
    }

    @Override
    public void process(Request request, Environment environment, Flow flow) {
        PrefixMatchEndpoint storedEndpoint =
                endpoints.getOrDefault(request.getRequester().getUniqueID(), null);
        PrefixMatchEndpoint forcedEndpoint = getForcedEndpoint(request, environment);

        if (forcedEndpoint != null) {
            ISpeaker requester = request.getRequester();

            if (storedEndpoint != forcedEndpoint) {
                endpoints.put(requester.getUniqueID(), forcedEndpoint);
                sendSetEndpointMessage(requester, forcedEndpoint);
            } else {
                endpoints.remove(requester.getUniqueID());
                sendSetMessage(requester, Translations.ENDPOINT_RESET);
            }

            return;
        }


        if (storedEndpoint != null)
            environment.getState().setValue(FORCED_ENDPOINT, storedEndpoint);

        flow.next();
    }

    private PrefixMatchEndpoint getForcedEndpoint(Request request, Environment environment) {

        for (Middleware middleware : environment.getCore().getMiddleware()) {
            if (middleware instanceof PrefixMatchEndpoint) {
                PrefixMatchEndpoint endpoint = (PrefixMatchEndpoint) middleware;

                if (endpoint.matchEndpoint(request, environment)) {
                    Environment clone = environment.clone();
                    endpoint.removePrefix(clone);
                    String text = clone.getState().getValue(Environment.TEXT);

                    if (text.trim().isEmpty()) {
                        return endpoint;
                    }
                }
            }
        }

        return null;
    }

    private void sendSetEndpointMessage(ISpeaker requester, PrefixMatchEndpoint endpoint) {
        sendSetMessage(requester, String.format(Translations.ENDPOINT_SET, endpoint.getName()));
    }
}
