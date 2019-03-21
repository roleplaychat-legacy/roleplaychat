package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

import java.util.HashMap;
import java.util.Map;

public class GmOOCEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.GOLD);
        colors.put("username", TextFormatting.GREEN);
        colors.put("label", TextFormatting.WHITE);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/gm_ooc.twig");

    public GmOOCEndpoint() throws EmptyPrefixException {
        super("-");
    }

    @Override public void processEndpoint(Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
        environment.getRecipients().clear();
    }

    @Override public void process(Request request, Environment environment) {
        super.process(request, environment);
        environment.getRecipients().add(request.getRequester());
    }
}
