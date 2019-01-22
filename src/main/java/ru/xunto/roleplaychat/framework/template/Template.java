package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.template.tokens.TextToken;
import ru.xunto.roleplaychat.framework.template.tokens.Token;
import ru.xunto.roleplaychat.framework.template.tokens.VariableToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Template implements ITemplate {
    private List<Token> tokens = new ArrayList<>();

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

            tokens.add(new TextToken(text));

            String varDefault = "undefined";
            if (var.contains("|")) {
                String[] split = var.split(Pattern.quote("|"), 2);
                var = split[0].trim();
                varDefault = split[1].trim();
            }

            tokens.add(new VariableToken(var, varDefault));
        }

        tokens.add(new TextToken(left));
    }

    public TextComponentBase build(MessageState state, Map<String, TextFormatting> colors) {
        TextComponentBase parts = new TextComponentString("");
        parts.getStyle().setColor(colors.getOrDefault("default", TextFormatting.WHITE));

        for (Token token : tokens) {
            parts.appendSibling(token.build(state, colors));
        }
        return parts;
    }
}
