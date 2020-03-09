package ru.xunto.roleplaychat.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;

public class CompatPlayerChatEvent extends AsyncPlayerChatEvent {
    public CompatPlayerChatEvent(Player player, String message) {
        super(true, player, message, new HashSet<>());
    }
}
