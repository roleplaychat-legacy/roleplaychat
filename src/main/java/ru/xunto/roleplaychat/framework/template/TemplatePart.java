package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public abstract class TemplatePart {
    public abstract TextComponentBase build(Map<String, String> values,
        Map<String, TextFormatting> colors);
}
