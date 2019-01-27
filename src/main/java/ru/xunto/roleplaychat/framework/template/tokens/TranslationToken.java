package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class TranslationToken extends VariableToken {
    public TranslationToken(String varName, String varDefault) {
        super(varName, varDefault);
    }

    @Override protected ITextComponent createComponent(String text, TextFormatting color) {
        ITextComponent component = new TextComponentTranslation(text);
        component.getStyle().setColor(color);
        return component;
    }
}
