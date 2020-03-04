package ru.xunto.roleplaychat.forge_1_7_10;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;

import java.util.List;

public class ForgeWorld implements IWorld {
    private WorldServer world;

    public ForgeWorld(WorldServer world) {
        this.world = world;
    }

    @Override
    public IServer getServer() {
        return new ForgeServer(FMLCommonHandler.instance().getMinecraftServerInstance());
    }

    @Override
    public ISpeaker[] getPlayers() {
        List<EntityPlayerMP> playerEntities = world.playerEntities;
        return playerEntities.stream().map(ForgeSpeaker::new).toArray(ISpeaker[]::new);
    }
}
