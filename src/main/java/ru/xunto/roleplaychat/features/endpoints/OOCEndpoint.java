package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

import java.util.HashMap;
import java.util.Map;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - TextFormatting
*/

public class OOCEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("username", TextFormatting.GREEN);
        colors.put("default", TextFormatting.LIGHT_PURPLE);
        colors.put("label", TextFormatting.WHITE);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/ooc.twig");

    public OOCEndpoint() throws EmptyPrefixException {
        super("_");
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
