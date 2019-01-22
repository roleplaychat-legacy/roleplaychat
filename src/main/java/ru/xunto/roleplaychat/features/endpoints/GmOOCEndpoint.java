package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.MessageState;
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

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return environment.getState().getValue(Core.TEXT).startsWith("-");
    }

    @Override public void processEndpoint(Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Core.TEXT).substring(1).trim();
        state.setValue(Core.TEXT, text);

        environment.setTemplate(template);
        environment.getColors().putAll(colors);
        environment.getRecipients().clear();
    }
}
