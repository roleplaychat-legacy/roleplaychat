package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class DefaultEndpoint extends Endpoint {
    private Template template = new Template("{{ username }}: {{ text }}", TextFormatting.WHITE);

    @Override public boolean matchEndpoint(Environment environment) {
        return true;
    }

    @Override public void processEndpoint(Environment response) {
        Map<String, TextFormatting> colors = new HashMap<>();
        colors.put("username", TextFormatting.GREEN);

        response.setComponent(template.build(response.variables, colors));
    }

    @Override public Priority getPriority() {
        return Priority.LOWEST;
    }
}
