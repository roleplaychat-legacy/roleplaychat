package ru.xunto.roleplaychat.api;

public interface IWorld {
    IServer getServer();

    ISpeaker[] getPlayers();
}
