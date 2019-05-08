package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

import java.util.HashMap;
import java.util.Map;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - TextFormatting
            - PermissionAPI
*/

public class GmActionEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.YELLOW);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/gm_action.twig");

    public GmActionEndpoint() throws EmptyPrefixException {
        super("#", "№");
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return super.matchEndpoint(request, environment) && PermissionAPI
            .hasPermission(request.getRequester(), "gm");
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
