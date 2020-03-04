package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - EntityPlayer
            - World
*/


public class Request {
    private final String text;
    private final ISpeaker requester;
    private final IWorld world;

    public Request(String text, ISpeaker requester, IWorld world) {
        this.text = text;
        this.requester = requester;
        this.world = world;
    }

    public IWorld getWorld() {
        return world;
    }

    public ISpeaker getRequester() {
        return requester;
    }

    public String getText() {
        return text;
    }

    public IServer getServer() {
        return world.getServer();
    }
}
