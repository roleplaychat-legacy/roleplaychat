package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Template {
    private List<TemplatePart> tokens = new ArrayList<>();

    public Template(String template) {
        String left = template;

        while (true) {
            int begin = left.indexOf("{{");
            int end = left.indexOf("}}");

            if ((begin == -1) || (end == -1))
                break;

            String text = left.substring(0, begin);
            String var = left.substring(begin + 2, end).trim();
            left = left.substring(end + 2);

            tokens.add(new TextTemplatePart(text));

            String varDefault = "undefined";
            if (var.contains("|")) {
                String[] split = var.split(Pattern.quote("|"), 2);
                var = split[0];
                varDefault = split[1];
            }

            tokens.add(new VariableTemplatePart(var, varDefault));
        }

        tokens.add(new TextTemplatePart(left));
    }

    public TextComponentBase build(Map<String, String> values, Map<String, TextFormatting> colors) {
        TextComponentBase parts = new TextComponentString("");
        parts.getStyle().setColor(colors.getOrDefault("default", TextFormatting.WHITE));

        for (TemplatePart token : tokens) {
            parts.appendSibling(token.build(values, colors));
        }
        return parts;
    }
}
