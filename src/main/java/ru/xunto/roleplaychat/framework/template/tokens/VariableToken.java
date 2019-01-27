package ru.xunto.roleplaychat.framework.template.tokens;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

public class VariableToken extends Token {
    private String varName;
    private String varDefault;

    public VariableToken(String varName, String varDefault) {
        this.varName = varName;
        this.varDefault = varDefault;
    }

    protected ITextComponent createComponent(String text, TextFormatting color) {
        ITextComponent component = ForgeHooks.newChatWithLinks(text);
        component.getStyle().setColor(color);
        return component;
    }

    @Override public ITextComponent build(MessageState state, Map<String, TextFormatting> colors) {
        String text = state.getValue(varName);
        if (text == null)
            text = varDefault;
        if (text == null)
            text = varName;

        return this.createComponent(text, colors.get(this.varName));
    }
}
