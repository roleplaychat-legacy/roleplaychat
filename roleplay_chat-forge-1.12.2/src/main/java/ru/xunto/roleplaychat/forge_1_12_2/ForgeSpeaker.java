package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.PermissionAPI;
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
        player.sendMessage(RoleplayChat.createComponent(text, color));
    }

    @Override
    public void sendMessage(Text text) {
        player.sendMessage(RoleplayChat.toTextComponent(text));
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public Position getPosition() {
        BlockPos position = player.getPosition();
        return new Position(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public IWorld getWorld() {
        return new ForgeWorld(player.getServerWorld());
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
        return PermissionAPI.hasPermission(this.player, permission);
    }
}
