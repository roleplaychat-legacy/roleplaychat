package ru.xunto.roleplaychat.api;

import ru.xunto.roleplaychat.framework.renderer.text.Text;

public interface IHandler {
    ISpeaker getSpeaker(Object object);

    IWorld getWorld(Object object);

    IServer getServer(Object object);

    void log(Text text);

    boolean compat(ISpeaker speaker, Text text);
}
