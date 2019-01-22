package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class GmOOCEndpoint extends PrefixEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.GOLD);
        colors.put("username", TextFormatting.GREEN);
        colors.put("label", TextFormatting.WHITE);
    }

    private Template template = new Template("{{ username }} {{ label | (GM): }} (( {{ text }} ))");

    public GmOOCEndpoint() {
        super("-");
    }

    @Override public void processEndpoint(Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
        environment.getRecipients().clear();
    }
}
