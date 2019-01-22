package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

public abstract class Token {
    public abstract ITextComponent build(MessageState state, Map<String, TextFormatting> colors);
}
