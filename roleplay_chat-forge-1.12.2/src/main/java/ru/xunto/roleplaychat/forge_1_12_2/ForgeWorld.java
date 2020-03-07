package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;

public class ForgeWorld implements IWorld {
    private WorldServer world;

    public ForgeWorld(WorldServer world) {
        this.world = world;
    }

    @Override
    public IServer getServer() {
        return new ForgeServer(world.getMinecraftServer());
    }

    @Override
    public ISpeaker[] getPlayers() {
        return world.playerEntities.stream().map(
                (p) -> (EntityPlayerMP) p
        ).map(ForgeSpeaker::new).toArray(ISpeaker[]::new);
    }
}
