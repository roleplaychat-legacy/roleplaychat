package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public class VariableTemplatePart extends TemplatePart {
    private String var;
    private String varDefault;

    VariableTemplatePart(String var, String varDefault) {
        this.var = var;
        this.varDefault = varDefault;
    }

    @Override
    public TextComponentBase build(Map<String, String> values, Map<String, TextFormatting> colors) {
        TextComponentBase component =
            new TextComponentString(values.getOrDefault(this.var, varDefault));
        component.getStyle().setColor(colors.get(this.var));
        return component;
    }
}
