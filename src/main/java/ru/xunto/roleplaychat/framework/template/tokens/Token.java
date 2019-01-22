package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public abstract class Token {
    public abstract ITextComponent build(Map<String, String> values,
        Map<String, TextFormatting> colors);
}
