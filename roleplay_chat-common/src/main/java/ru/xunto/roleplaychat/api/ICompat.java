package ru.xunto.roleplaychat.api;

import ru.xunto.roleplaychat.framework.renderer.text.Text;

public interface ICompat {
    boolean compat(ISpeaker speaker, Text text);
}
