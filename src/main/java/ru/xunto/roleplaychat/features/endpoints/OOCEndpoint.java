package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.Map;

public class OOCEndpoint extends PrefixEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("username", TextFormatting.GREEN);
        colors.put("default", TextFormatting.LIGHT_PURPLE);
        colors.put("label", TextFormatting.WHITE);
    }

    private ITemplate template =
        new Template("{{ username }} {{ label | (OOC)}}: (( {{ text }} ))");

    public OOCEndpoint() throws EmptyPrefixException {
        super("_");
    }

    @Override public void processEndpoint(Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);
    }
}
