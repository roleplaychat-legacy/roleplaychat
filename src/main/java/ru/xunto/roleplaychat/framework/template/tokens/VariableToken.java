package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;

import java.util.Map;

public class VariableToken extends Token {
    private String var;
    private String varDefault;

    public VariableToken(String var, String varDefault) {
        this.var = var;
        this.varDefault = varDefault;
    }

    @Override
    public ITextComponent build(Map<String, String> values, Map<String, TextFormatting> colors) {
        String text = values.getOrDefault(this.var, varDefault);
        ITextComponent component = ForgeHooks.newChatWithLinks(text);
        component.getStyle().setColor(colors.get(this.var));
        return component;
    }
}
