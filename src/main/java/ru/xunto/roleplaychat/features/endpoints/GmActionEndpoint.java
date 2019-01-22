package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class GmActionEndpoint extends PrefixEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.YELLOW);
    }

    private ITemplate template = new Template("*** {{text}} ***");

    public GmActionEndpoint() throws EmptyPrefixException {
        super("#", "№");
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return super.matchEndpoint(request, environment) && PermissionAPI
            .hasPermission(request.getRequester(), "gm");
    }

    @Override public void processEndpoint(Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
