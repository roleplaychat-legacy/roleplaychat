package ru.xunto.roleplaychat.features.middleware.remember;

import net.minecraft.entity.player.EntityPlayer;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.Distance;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.api.Stage;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

import java.util.HashMap;
import java.util.UUID;

import static ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.DISTANCE;

public class RecallDistanceMiddleware extends AbstractRecallMiddleware {
    private HashMap<UUID, Distance> ranges = new HashMap<>();

    @Override public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
    }

    @Override public void process(Request request, Environment environment, Flow flow) {
        Distance storedRange = ranges.getOrDefault(request.getRequester().getUniqueID(), null);

        if (isSetRequest(request.getText(), new String[] {"!", "="})) {
            EntityPlayer requester = request.getRequester();
            Distance forcedRange = DistanceMiddleware.processDistanceState(request, environment);

            if (storedRange != forcedRange) {
                ranges.put(requester.getUniqueID(), forcedRange);
                sendSetDistanceMessage(requester, forcedRange);
            } else {
                ranges.remove(requester.getUniqueID());
                sendSetMessage(requester, Translations.DISTANCE_RESET);
            }

            return;
        }


        if (storedRange != null)
            environment.getState().setValue(DISTANCE, storedRange);

        flow.next();
    }

    private void sendSetDistanceMessage(EntityPlayer requester, Distance distance) {
        sendSetMessage(requester,
            String.format(Translations.DISTANCE_SET, Translations.stringifyDistance(distance)));
    }
}
