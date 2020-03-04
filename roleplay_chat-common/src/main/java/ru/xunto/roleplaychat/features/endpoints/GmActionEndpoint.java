package ru.xunto.roleplaychat.features.endpoints;

import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.HashMap;
import java.util.Map;

public class GmActionEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextColor> colors = new HashMap<>();

    static {
        colors.put("default", TextColor.YELLOW);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/gm_action.twig");

    public GmActionEndpoint() throws EmptyPrefixException {
        super("#", "№");
    }

    @Override
    public boolean matchEndpoint(Request request, Environment environment) {
        return super.matchEndpoint(request, environment) && request.getRequester().hasPermission("gm");
    }

    @Override
    public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
