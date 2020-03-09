package ru.xunto.roleplaychat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.List;
import java.util.logging.Logger;

public final class RoleplayChat extends JavaPlugin implements Listener {
    Logger logger = Logger.getLogger("RoleplayChat");

    public static ChatColor toMinecraftFormatting(TextColor color) {
        for (ChatColor value : ChatColor.values()) {
            if (value.name().equals(color.name())) return value;
        }

        return ChatColor.WHITE;
    }

    public static String createComponent(String content, TextColor color) {
        return RoleplayChat.toMinecraftFormatting(color) + content;
    }

    public static String toTextComponent(Text text) {
        Object cache = text.getCache();
        if (cache instanceof String) return (String) cache;

        StringBuilder builder = new StringBuilder();
        builder.append(RoleplayChat.toMinecraftFormatting(text.getDefaultColor()));

        for (TextComponent component : text.getComponents()) {
            builder.append(RoleplayChat.toMinecraftFormatting(component.getColor()));
            builder.append(component.getContent());
        }

        String result = builder.toString();
        text.setCache(result);
        return result;
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        if (event instanceof CompatPlayerChatEvent)
            return;

        List<Text> texts = RoleplayChatCore.instance.process(
                new Request(event.getMessage(), new SpigotSpeaker(event.getPlayer()))
        );

        event.setCancelled(true);

        for (Text text : texts) {
            String component = text.getUnformattedText();
            CompatPlayerChatEvent compatEvent = new CompatPlayerChatEvent(event.getPlayer(), component);
            Bukkit.getPluginManager().callEvent(compatEvent);
            if (!compatEvent.isCancelled()) logger.info(component);
        }
    }

    @Override
    public void onEnable() {
        PluginManager manager = getServer().getPluginManager();
        RoleplayChatCore.instance.warmUpRenderer();
        manager.registerEvents(this, this);

        // TODO: Add commands
    }

    @Override
    public void onDisable() {
    }
}
