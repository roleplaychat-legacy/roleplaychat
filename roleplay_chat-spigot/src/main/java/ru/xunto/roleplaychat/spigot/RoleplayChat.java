package ru.xunto.roleplaychat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.IPermission;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.List;
import java.util.logging.Logger;

public final class RoleplayChat extends JavaPlugin implements Listener {
    private Logger logger = Logger.getLogger("RoleplayChat");
    private RuntimeCommandRegistration commands = new RuntimeCommandRegistration(this);

    public static ChatColor toMinecraftFormatting(TextColor color) {
        for (ChatColor value : ChatColor.values()) {
            if (value.name().equals(color.name())) return value;
        }

        return ChatColor.WHITE;
    }

    public static String createComponent(String content, TextColor color) {
        ChatColor chatColor = RoleplayChat.toMinecraftFormatting(color);
        return chatColor + content.replace(ChatColor.RESET.toString(), chatColor.toString());
    }

    public static String toTextComponent(Text text) {
        Object cache = text.getCache();
        if (cache instanceof String) return (String) cache;

        StringBuilder builder = new StringBuilder();
        builder.append(RoleplayChat.toMinecraftFormatting(text.getDefaultColor()));

        for (TextComponent component : text.getComponents()) {
            builder.append(RoleplayChat.toMinecraftFormatting(component.getColor()));
            builder.append(component.getContent());
            builder.append(RoleplayChat.toMinecraftFormatting(text.getDefaultColor()));
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
            if (!compatEvent.isCancelled()) logger.info(ChatColor.stripColor(component));
        }
    }

    @Override
    public void onEnable() {
        PluginManager manager = getServer().getPluginManager();
        RoleplayChatCore.instance.warmUpRenderer();
        manager.registerEvents(this, this);

        for (IPermission permission : RoleplayChatCore.instance.getPermissions()) {
            manager.addPermission(new Permission(
                    permission.getName(),
                    permission.getDescription(),
                    PermissionDefault.FALSE
            ));
        }

        for (ICommand command : RoleplayChatCore.instance.getCommands()) {
            this.commands.registerCommand(command.getCommandName());
            this.getCommand(command.getCommandName()).setExecutor(new SpigotCommandExecutor(command));
        }
    }

    @Override
    public void onDisable() {
    }
}
