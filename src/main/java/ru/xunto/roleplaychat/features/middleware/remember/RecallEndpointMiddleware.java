package ru.xunto.roleplaychat.features.middleware.remember;

import net.minecraft.entity.player.EntityPlayer;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.framework.api.*;

import java.util.HashMap;

import static ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint.FORCED_ENDPOINT;

public class RecallEndpointMiddleware extends AbstractRecallMiddleware {
    private HashMap<EntityPlayer, Class<? extends PrefixMatchEndpoint>> endpoints =
        new HashMap<EntityPlayer, Class<? extends PrefixMatchEndpoint>>();

    @Override public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
    }

    private void sendSetEndpointMessage(EntityPlayer requester,
        Class<? extends PrefixMatchEndpoint> endpoint) {
        sendSetMessage(requester,
            String.format(Translations.ENDPOINT_SET, endpoint.getSimpleName()));
    }


    private Class<? extends PrefixMatchEndpoint> getForcedEndpoint(Request request,
        Environment environment) {
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

    @Override public void process(Request request, Environment environment) {
        Class<? extends PrefixMatchEndpoint> storedEndpoint =
            endpoints.getOrDefault(request.getRequester(), null);
        Class<? extends PrefixMatchEndpoint> forcedEndpoint =
            getForcedEndpoint(request, environment);

        if (forcedEndpoint != null) {
            EntityPlayer requester = request.getRequester();

            if (storedEndpoint != forcedEndpoint) {
                endpoints.put(requester, forcedEndpoint);
                sendSetEndpointMessage(requester, forcedEndpoint);
            } else {
                endpoints.remove(requester);
                sendSetMessage(requester, Translations.ENDPOINT_RESET);
            }

            environment.interrupt();
        }


        if (storedEndpoint != null)
            environment.getState().setValue(FORCED_ENDPOINT, storedEndpoint);
    }
}
