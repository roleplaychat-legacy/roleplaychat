package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
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
        String text = environment.getVariables().get("text");
        return (text.startsWith("#") || text.startsWith("№")) && PermissionAPI
            .hasPermission(request.getRequester(), "gm");
    }

    @Override public void processEndpoint(Environment environment) {
        Map<String, String> variables = environment.getVariables();
        String text = variables.get("text").substring(1).trim();

        environment.setTemplate(template);
        variables.put("text", text);
        environment.getColors().putAll(colors);
    }
}
