package ru.xunto.roleplaychat.forge_1_7_10;

import net.minecraft.server.MinecraftServer;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.IWorld;

import java.util.Arrays;

public class ForgeServer implements IServer {
    private MinecraftServer server;

    public ForgeServer(MinecraftServer minecraftServer) {
        this.server = minecraftServer;
    }

    @Override
    public IWorld[] getWorlds() {
        return Arrays.stream(server.worldServers).map(ForgeWorld::new).toArray(IWorld[]::new);
    }
}
