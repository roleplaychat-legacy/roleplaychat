package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    private Template template = new Template("* {{ username }} {{ text }}", TextFormatting.WHITE);

    @Override public boolean matchEndpoint(Environment environment) {
        return environment.variables.get("text").startsWith("*");
    }

    @Override public void processEndpoint(Environment environment) {
        Map<String, TextFormatting> colors = new HashMap<>();
        colors.put("username", TextFormatting.GREEN);

        String text = environment.variables.get("text").substring(1).trim();
        environment.variables.put("text", text);
        environment.setComponent(template.build(environment.variables, colors));
    }
}
