package ru.xunto.roleplaychat.features.middleware.distance;

import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.IHearingMode;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.InfiniteHearingMode;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.NoExtraHearingMode;
import ru.xunto.roleplaychat.features.permissions.PermissionGM;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.api.Stage;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ListenMiddleware extends Middleware {
    // Should msg bypass Listen
    public final static IProperty<Boolean> AVOID = new Property<>("avoid_listen");

    private static Map<UUID, IHearingMode> hearingModes = new HashMap<>();

    public static IHearingMode getHearingMode(ISpeaker speaker) {
        IHearingMode mode = hearingModes.getOrDefault(speaker.getUniqueID(), null);

        if (mode != null) return mode;
        if (speaker.hasPermission(PermissionGM.instance)) return InfiniteHearingMode.instance;

        return NoExtraHearingMode.instance;
    }

    public static void setHearingMode(ISpeaker speaker, IHearingMode mode) {
        ListenMiddleware.hearingModes.put(speaker.getUniqueID(), mode);
    }

    public static void resetHearingMode(ISpeaker speaker) {
        hearingModes.remove(speaker.getUniqueID());
    }

    @Override
    public Stage getStage() {
        return Stage.POST;
    }

    @Override
    public void process(Request request, Environment environment, Flow flow) {
        Boolean avoid = environment.getState().getValue(AVOID, false);
        if (avoid) {
            flow.next();
            return;
        }

        Environment newEnvironment = environment.clone();

        Set<ISpeaker> originalRecipients = environment.getRecipients();
        Set<ISpeaker> recipients = newEnvironment.getRecipients();
        recipients.clear();

        newEnvironment.getColors().clear();
        newEnvironment.getColors().put("default", TextColor.GRAY);

        IServer server = request.getRequester().getWorld().getServer();
        IWorld[] worlds = server.getWorlds();

        for (IWorld world : worlds) {
            for (ISpeaker player : world.getPlayers()) {
                boolean allowed = ListenMiddleware.getHearingMode(player).canAvoidHearingRestriction(
                        player,
                        request.getRequester()
                );

                if (allowed && !originalRecipients.contains(player))
                    recipients.add(player);
            }
        }

        if (recipients.size() > 0)
            flow.lightFork(newEnvironment);

        flow.next();
    }
}
