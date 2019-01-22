package ru.xunto.roleplaychat.framework.template;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

public interface ITemplate {
    TextComponentBase build(MessageState state, Map<String, TextFormatting> colors);
}
