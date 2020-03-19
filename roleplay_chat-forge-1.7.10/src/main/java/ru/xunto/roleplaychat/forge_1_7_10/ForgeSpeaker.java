package ru.xunto.roleplaychat.forge_1_7_10;

import net.minecraft.entity.player.EntityPlayerMP;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.Objects;
import java.util.UUID;

public class ForgeSpeaker implements ISpeaker {
    private EntityPlayerMP player;

    public ForgeSpeaker(EntityPlayerMP player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String text, TextColor color) {
        player.addChatMessage(RoleplayChat.createComponent(text, color));
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
    public String getRealName() {
        return player.getCommandSenderName();
    }

    @Override
    public Position getPosition() {
        return new Position((int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public IWorld getWorld() {
        return new ForgeWorld(player.getServerForPlayer());
    }

    @Override
    public UUID getUniqueID() {
        return player.getUniqueID();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.canCommandSenderUseCommand(2, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForgeSpeaker)) return false;
        ForgeSpeaker speaker = (ForgeSpeaker) o;
        return Objects.equals(player, speaker.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
