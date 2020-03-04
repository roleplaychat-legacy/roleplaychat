package ru.xunto.roleplaychat.forge_1_7_10;

import net.minecraft.entity.player.EntityPlayerMP;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.framework.renderer.text.Text;

import java.util.Objects;
import java.util.UUID;

public class ForgeSpeaker implements ISpeaker {
    private EntityPlayerMP player;

    public ForgeSpeaker(EntityPlayerMP player) {
        this.player = player;
    }

    @Override
    public void sendMessage(Text text) {
        player.addChatMessage(RoleplayChat.toTextComponent(text));
    }

    @Override
    public String getName() {
        return player.getDisplayName();
    }

    @Override
    public Position getPosition() {
        return new Position((int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public UUID getUniqueID() {
        return player.getUniqueID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForgeSpeaker)) return false;
        ForgeSpeaker that = (ForgeSpeaker) o;

        return that.getUniqueID() == that.getUniqueID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.canCommandSenderUseCommand(2, "");
    }
}
