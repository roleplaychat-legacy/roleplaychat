package ru.xunto.roleplaychat.features.middleware.remember;

import net.minecraft.entity.player.EntityPlayer;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.Distance;
import ru.xunto.roleplaychat.framework.api.*;

import java.util.HashMap;

import static ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.DISTANCE;

public class RecallDistanceMiddleware extends AbstractRecallMiddleware {
    private HashMap<EntityPlayer, Distance> ranges = new HashMap<>();

    @Override public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
    }

    @Override public void process(Request request, Environment environment) throws ChatException {
        Distance storedRange = ranges.getOrDefault(request.getRequester(), null);

        if (isSetRequest(request.getText(), new String[] {"!", "="})) {
            Distance forcedRange = DistanceMiddleware.processDistanceState(request, environment);
            if (storedRange != forcedRange) {
                ranges.put(request.getRequester(), forcedRange);
                throw new ChatException("Set distance: " + forcedRange.toString());
            } else {
                ranges.remove(request.getRequester());
                throw new ChatException("Distance was reset");
            }
        }

        if (storedRange != null)
            environment.getState().setValue(DISTANCE, storedRange);
    }
}
