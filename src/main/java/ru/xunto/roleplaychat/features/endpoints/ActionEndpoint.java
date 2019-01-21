package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.features.LabeledTemplate;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.GRAY);
        colors.put("text", TextFormatting.WHITE);
        colors.put("username", TextFormatting.GREEN);
    }

    private ITemplate template =
        new LabeledTemplate(new Template("* {{ username }} {{ action }} * {{ text }}"),
            new Template("* {{ username }} {{ label }}  {{ action }} * {{ text }}"));

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return environment.getVariables().get("text").startsWith("*");
    }

    @Override public void processEndpoint(Environment environment) {
        Map<String, String> variables = environment.getVariables();
        String text = variables.get("text").substring(1).trim();

        String[] strings = text.split("\\*");

        environment.setTemplate(template);
        variables.put("action", strings[0].trim());
        variables.put("text", strings.length > 1 ? strings[1].trim() : "");
        environment.getColors().putAll(colors);
    }
}
