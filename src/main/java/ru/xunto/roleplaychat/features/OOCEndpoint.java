package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class OOCEndpoint extends Endpoint {
    private Template template = new Template("{{ username }} {{label|(OOC):}} (( {{ text }} ))",
        TextFormatting.LIGHT_PURPLE);

    @Override public boolean matchEndpoint(Environment environment) {
        return environment.variables.get("text").startsWith("_");
    }

    @Override public void processEndpoint(Environment environment) {
        Map<String, TextFormatting> colors = new HashMap<>();
        colors.put("username", TextFormatting.GREEN);
        colors.put("label", TextFormatting.WHITE);

        String text = environment.variables.get("text").substring(1).trim();
        environment.variables.put("text", text);
        environment.setComponent(template.build(environment.variables, colors));
    }
}
