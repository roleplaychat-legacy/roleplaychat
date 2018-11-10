package ru.xunto.roleplaychat.forge;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Request;

@Mod(modid = RoleplayChat.MODID, name = RoleplayChat.NAME, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        ITextComponent component = Core.instance.process(
            new Request(event.getMessage(), event.getPlayer(), event.getPlayer().getServerWorld()));

        event.setComponent(component);
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
