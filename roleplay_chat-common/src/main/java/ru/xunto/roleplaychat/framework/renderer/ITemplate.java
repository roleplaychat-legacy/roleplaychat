package ru.xunto.roleplaychat.framework.renderer;

import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.Map;

public interface ITemplate {
    Text render(MessageState state, Map<String, TextColor> colors);
}
