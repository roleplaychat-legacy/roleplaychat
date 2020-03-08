package ru.xunto.roleplaychat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.World;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;

public class SpigotWorld implements IWorld {
    private World world;

    public SpigotWorld(World world) {
        this.world = world;
    }

    @Override
    public IServer getServer() {
        return new SpigotServer(Bukkit.getServer());
    }

    @Override
    public ISpeaker[] getPlayers() {
        return world.getPlayers().stream().map(SpigotSpeaker::new).toArray(ISpeaker[]::new);
    }
}
