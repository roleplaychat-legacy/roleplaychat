package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class GmOOCEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextColor> colors = new HashMap<>();

    static {
        colors.put("default", TextColor.GOLD);
        colors.put("username", TextColor.GREEN);
        colors.put("label", TextColor.WHITE);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/gm_ooc.twig");

    public GmOOCEndpoint() throws EmptyPrefixException {
        super("-");
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);

        Set<ISpeaker> recipients = environment.getRecipients();
        recipients.clear();

        IWorld[] worlds = request.getServer().getWorlds();
        for (IWorld world : worlds) {
            for (ISpeaker player : world.getPlayers()) {
                if (player.hasPermission("gm"))
                    recipients.add(player);
            }
        }

        recipients.add(request.getRequester());
    }
}
