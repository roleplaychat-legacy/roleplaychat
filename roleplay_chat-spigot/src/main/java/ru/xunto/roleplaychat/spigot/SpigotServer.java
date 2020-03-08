package ru.xunto.roleplaychat.spigot;

import org.bukkit.Server;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.IWorld;

public class SpigotServer implements IServer {
    private Server server;

    public SpigotServer(Server server) {
        this.server = server;
    }

    @Override
    public IWorld[] getWorlds() {
        return server.getWorlds().stream().map(SpigotWorld::new).toArray(IWorld[]::new);
    }
}
