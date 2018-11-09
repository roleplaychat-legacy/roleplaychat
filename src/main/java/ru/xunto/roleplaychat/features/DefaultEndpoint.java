package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class DefaultEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("username", TextFormatting.GREEN);
    }

    private Template template = new Template("{{ username }}: {{ text }}");

    @Override public boolean matchEndpoint(Environment environment) {
        return true;
    }

    @Override public void processEndpoint(Environment response) {
        response.setTemplate(template);
        response.getColors().putAll(colors);
    }

    @Override public Priority getPriority() {
        return Priority.LOWEST;
    }
}
