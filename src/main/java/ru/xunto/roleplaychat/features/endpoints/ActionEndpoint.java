package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.features.LabeledTemplate;
import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Endpoint;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class ActionEndpoint extends Endpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();
    public static final IProperty<String> ACTION = new Property<>("action");

    static {
        colors.put("default", TextFormatting.GRAY);
        colors.put("text", TextFormatting.WHITE);
        colors.put("username", TextFormatting.GREEN);
    }

    private ITemplate template =
        new LabeledTemplate(new Template("* {{ username }} {{ action }} * {{ text }}"),
            new Template("* {{ username }} {{ label }}  {{ action }} * {{ text }}"));

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return environment.getState().getValue(Core.TEXT).startsWith("*");
    }

    @Override public void processEndpoint(Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Core.TEXT).substring(1).trim();

        String[] strings = text.split("\\*");

        environment.setTemplate(template);

        state.setValue(ACTION, strings[0].trim());
        state.setValue(Core.TEXT, strings.length > 1 ? strings[1].trim() : "");

        environment.getColors().putAll(colors);
    }
}
