package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class GmActionEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.YELLOW);
    }

    private ITemplate template = new Template("*** {{text}} ***");

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        String text = environment.getState().getValue(Core.TEXT);
        return (text.startsWith("#") || text.startsWith("№")) && PermissionAPI
            .hasPermission(request.getRequester(), "gm");
    }

    @Override public void processEndpoint(Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Core.TEXT).substring(1).trim();
        state.setValue(Core.TEXT, text);

        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
