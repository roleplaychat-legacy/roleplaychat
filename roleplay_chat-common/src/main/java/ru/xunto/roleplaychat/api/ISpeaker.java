package ru.xunto.roleplaychat.api;

import ru.xunto.roleplaychat.framework.renderer.text.Text;

import java.util.UUID;

public interface ISpeaker {
    void sendMessage(Text components);

    String getName();

    Position getPosition();

    IWorld getWorld();

    UUID getUniqueID();

    boolean hasPermission(String permission);
}
