package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public class TextTemplatePart extends TemplatePart {
    private String text;

    TextTemplatePart(String text) {
        this.text = text;
    }

    @Override
    public ITextComponent build(Map<String, String> values, Map<String, TextFormatting> colors) {
        return new TextComponentString(this.text);
    }
}
