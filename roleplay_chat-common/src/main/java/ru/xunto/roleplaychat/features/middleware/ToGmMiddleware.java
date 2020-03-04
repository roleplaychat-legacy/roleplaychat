package ru.xunto.roleplaychat.features.middleware;

import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.api.Stage;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.Set;

public class ToGmMiddleware extends Middleware {
    @Override public Stage getStage() {
        return Stage.POST;
    }

    @Override public void process(Request request, Environment environment, Flow flow) {
        Environment newEnvironment = environment.clone();

        Set<ISpeaker> originalRecipients = environment.getRecipients();
        Set<ISpeaker> recipients = newEnvironment.getRecipients();
        recipients.clear();

        newEnvironment.getColors().clear();
        newEnvironment.getColors().put("default", TextColor.DARK_GRAY);

        IServer server = request.getServer();
        IWorld[] worlds = server.getWorlds();

        for (IWorld world : worlds) {
            for (ISpeaker player : world.getPlayers()) {
                boolean allowed = player.hasPermission("gm");
                if (allowed && !originalRecipients.contains(player))
                    recipients.add(player);
            }
        }

        if (recipients.size() > 0)
            flow.lightFork(newEnvironment);

        flow.next();
    }
}
