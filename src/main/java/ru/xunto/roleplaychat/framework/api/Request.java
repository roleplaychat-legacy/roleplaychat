package ru.xunto.roleplaychat.framework.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Request {
    private String text;
    private EntityPlayer requester;
    private World world;

    public Request(String text, EntityPlayer requester, World world) {
        this.text = text;
        this.requester = requester;
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public EntityPlayer getRequester() {
        return requester;
    }

    public String getText() {
        return text;
    }
}
