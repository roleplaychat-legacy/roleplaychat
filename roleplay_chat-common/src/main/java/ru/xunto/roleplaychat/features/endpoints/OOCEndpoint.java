package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.HashMap;
import java.util.Map;

public class OOCEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextColor> colors = new HashMap<>();

    static {
        colors.put("default", TextColor.DARK_GRAY);
        colors.put("username", TextColor.DARK_GRAY);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/ooc.twig");

    public OOCEndpoint(RoleplayChatCore core) throws EmptyPrefixError {
        super(core, "_");
    }

    @Override
    public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
