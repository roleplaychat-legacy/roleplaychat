package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    public static final IProperty<String[]> ACTION_PARTS = new Property<>("action_parts");
    public static final IProperty<Boolean> START_WITH_ACTION = new Property<>("start_with_action");

    private static final Map<String, TextColor> colors = new HashMap<>();

    static {
        colors.put("username", TextColor.GREEN);
        colors.put("text", TextColor.WHITE);
        colors.put("action", TextColor.GRAY);
    }

    private ITemplate template = new PebbleChatTemplate("templates/action.pebble.twig");

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public boolean matchEndpoint(Request request, Environment environment) {
        return environment.getState().getValue(Environment.TEXT).contains("*");
    }

    @Override
    public void processEndpoint(Request request, Environment environment) {
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
