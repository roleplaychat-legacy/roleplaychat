package ru.xunto.roleplaychat.forge;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.CoreChat;
import ru.xunto.roleplaychat.framework.api.ChatException;
import ru.xunto.roleplaychat.framework.api.Request;

@Mod(modid = RoleplayChat.MODID, name = RoleplayChat.NAME, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    public final static CoreChat chat = new CoreChat();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        ITextComponent component = null;
        try {
            component = chat.process(new Request(event.getMessage(), event.getPlayer(),
                event.getPlayer().getServerWorld()));
        } catch (ChatException e) {
            component = new TextComponentString(e.getMessage());
            component.getStyle().setColor(TextFormatting.RED);
            event.getPlayer().sendMessage(component);
            e.printStackTrace();
        }

        event.setComponent(component);
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void cancelChatMessage(ServerChatEvent event) {
        event.setCanceled(true);
    }


    @Mod.EventHandler public void startServer(FMLServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        PermissionAPI.registerNode("gm", DefaultPermissionLevel.OP, "Game Master permission");
    }
}
