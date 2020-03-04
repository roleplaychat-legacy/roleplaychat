package ru.xunto.roleplaychat.framework.renderer;

import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

public interface ITemplate<E extends MessageState> {
    public Text render(E state, Map<String, TextColor> colors);
}
