package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public class TextToken extends Token {
    private String text;

    public TextToken(String text) {
        this.text = text;
    }

    @Override
    public ITextComponent build(Map<String, String> values, Map<String, TextFormatting> colors) {
        return new TextComponentString(this.text);
    }
}
