package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.features.LabeledTemplate;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("username", TextFormatting.GREEN);
    }

    private ITemplate template = new LabeledTemplate(new Template("* {{ username }} {{ text }}"),
        new Template("* {{ username }} ({{ label }}) {{ text }}"));

    @Override public boolean matchEndpoint(Environment environment) {
        return environment.getVariables().get("text").startsWith("*");
    }

    @Override public void processEndpoint(Environment environment) {
        String text = environment.getVariables().get("text").substring(1).trim();

        environment.setTemplate(template);
        environment.getVariables().put("text", text);
        environment.getColors().putAll(colors);
    }
}
