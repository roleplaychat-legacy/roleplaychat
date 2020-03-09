package ru.xunto.roleplaychat.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.List;

public final class RoleplayChat extends JavaPlugin implements Listener {
    public static String createComponent(String content, TextColor color) {
//        IChatComponent component = new ChatComponentText("").appendSibling(ForgeHooks.newChatWithLinks(content));
//        component.getChatStyle().setColor(RoleplayChat.toMinecraftFormatting(color));

        return content;
    }

    public static String toTextComponent(Text text) {
        Object cache = text.getCache();
        if (cache instanceof String) return (String) cache;

        StringBuilder builder = new StringBuilder();

        String result = text.getUnformattedText();

        text.setCache(result);
        return result;
    }

    @EventHandler
    public void onChatEvent(PlayerChatEvent event) {
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
        RoleplayChatCore.instance.warmUpRenderer();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }
}
