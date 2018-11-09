package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class GmOOCEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.GOLD);
        colors.put("username", TextFormatting.GREEN);
        colors.put("label", TextFormatting.WHITE);
    }

    private Template template = new Template("{{ username }} {{ label | (GM): }} (( {{ text }} ))");

    @Override public boolean matchEndpoint(Environment environment) {
        return environment.getVariables().get("text").startsWith("-");
    }

    @Override public void processEndpoint(Environment environment) {
        Map<String, String> variables = environment.getVariables();
        String text = variables.get("text").substring(1).trim();
        variables.put("text", text);

        environment.setTemplate(template);
        environment.getColors().putAll(colors);
        environment.getRecipients().clear();
    }
}
