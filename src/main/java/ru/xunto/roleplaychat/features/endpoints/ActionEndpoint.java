package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    public static final IProperty<String[]> ACTION_PARTS = new Property<>("action_parts");
    public static final IProperty<Boolean> START_WITH_ACTION = new Property<>("start_with_action");

    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("text", TextFormatting.WHITE);
        colors.put("action", TextFormatting.GRAY);
        colors.put("username", TextFormatting.GREEN);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/action.twig");

    @Override public Priority getPriority() {
        return Priority.LOW;
    }

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return environment.getState().getValue(Environment.TEXT).contains("*");
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        state.setValue(START_WITH_ACTION, text.startsWith("*"));

        if (text.startsWith("*")) {
            text = text.substring(1).trim();
        }

        String[] strings = text.split("\\*");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
        }

        state.setValue(ACTION_PARTS, strings);

        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }


}
