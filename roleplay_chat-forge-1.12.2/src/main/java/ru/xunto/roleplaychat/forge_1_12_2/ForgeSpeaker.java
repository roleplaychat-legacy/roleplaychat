package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.Objects;

public class ForgeSpeaker extends ForgeEntitySpeaker {
    private EntityPlayerMP player;

    public ForgeSpeaker(EntityPlayerMP player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return PermissionAPI.hasPermission(this.player, permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForgeSpeaker)) return false;
        ForgeSpeaker that = (ForgeSpeaker) o;
        return Objects.equals(player, that.player);
    }
}
