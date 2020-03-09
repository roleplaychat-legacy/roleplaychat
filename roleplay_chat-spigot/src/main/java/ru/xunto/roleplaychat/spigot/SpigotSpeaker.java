package ru.xunto.roleplaychat.spigot;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.xunto.roleplaychat.api.IPermission;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.Objects;
import java.util.UUID;

public class SpigotSpeaker implements ISpeaker {
    private Player player;

    public SpigotSpeaker(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String text, TextColor color) {
        String result = RoleplayChat.createComponent(text, color);
        this.player.sendMessage(result);
    }

    @Override
    public void sendMessage(Text text) {
        String result = RoleplayChat.toTextComponent(text);
        this.player.sendMessage(result);
    }

    @Override
    public String getName() {
        return this.player.getDisplayName();
    }

    @Override
    public String getRealName() {
        return player.getName();
    }

    @Override
    public Position getPosition() {
        Location location = this.player.getLocation();
        return new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public IWorld getWorld() {
        return new SpigotWorld(player.getWorld());
    }

    @Override
    public UUID getUniqueID() {
        return this.player.getUniqueId();
    }

    @Override
    public boolean hasPermission(IPermission permission) {
        return this.player.hasPermission(permission.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpigotSpeaker)) return false;
        SpigotSpeaker that = (SpigotSpeaker) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
