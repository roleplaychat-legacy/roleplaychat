package ru.xunto.roleplaychat.spigot;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;

import java.util.List;

public final class RoleplayChat extends JavaPlugin implements Listener {
    private static BaseComponent toTextComponent(Text text) {
        return null;
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
//        if (event instanceof CompatPlayerChatEvent)
//            return;

        List<Text> texts = RoleplayChatCore.instance.process(
                new Request(event.getMessage(), new SpigotSpeaker(event.getPlayer()))
        );

        event.setCancelled(true);

//        for (Text text : texts) {
//            BaseComponent component = RoleplayChat.toTextComponent(text);
//
//            Bukkit.getPluginManager().callEvent(new CompatPlayerChatEvent(true, event.getPlayer(), component.getInsertion(), component));
//            boolean isCanceled = EVENT_BUS.post(
//
//            );
//
//            if (!isCanceled) FMLCommonHandler.instance().getMinecraftServerInstance().sendMessage(component);
//        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }
}
