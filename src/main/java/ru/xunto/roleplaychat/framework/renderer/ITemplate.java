package ru.xunto.roleplaychat.framework.renderer;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - ITextComponent
            - TextFormatting
*/

public interface ITemplate<E extends MessageState> {
    public ITextComponent render(E state, Map<String, TextFormatting> colors);
}
