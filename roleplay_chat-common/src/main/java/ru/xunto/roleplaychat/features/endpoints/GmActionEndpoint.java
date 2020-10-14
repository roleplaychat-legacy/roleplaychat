package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.permissions.PermissionGM;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.HashMap;
import java.util.Map;

public class GmActionEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextColor> colors = new HashMap<>();

    static {
        colors.put("default", TextColor.DARK_AQUA);
    }

    private ITemplate template = new PebbleChatTemplate("templates/gm_action.pebble.twig");

    public GmActionEndpoint(RoleplayChatCore core) throws EmptyPrefixError {
        super(core, "#", "№");
    }

    @Override
    public boolean canSay(ISpeaker speaker) {
        return speaker.hasPermission(PermissionGM.instance);
    }

    @Override
    public boolean matchEndpoint(Request request, Environment environment) {
        return super.matchEndpoint(request, environment) && this.canSay(request.getRequester());
    }

    @Override
    public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
