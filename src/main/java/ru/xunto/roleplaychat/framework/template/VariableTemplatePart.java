package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;

import java.util.Map;

public class VariableTemplatePart extends TemplatePart {
    private String var;
    private String varDefault;

    VariableTemplatePart(String var, String varDefault) {
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
