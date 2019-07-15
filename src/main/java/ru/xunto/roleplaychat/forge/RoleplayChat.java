package ru.xunto.roleplaychat.forge;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.CoreChat;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(modid = RoleplayChat.MODID, name = RoleplayChat.NAME, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    public final static CoreChat chat = new CoreChat();

    public void createComaptEvent(ServerChatEvent event, ITextComponent component) {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        if (event instanceof CompatServerChatEvent)
            return;

        List<ITextComponent> components = chat.process(
            new Request(event.getMessage(), event.getPlayer(), event.getPlayer().getServerWorld()));

        event.setCanceled(true);

        for (ITextComponent component : components) {
            EVENT_BUS.post(
                new CompatServerChatEvent(event.getPlayer(), component.getUnformattedText(),
                    component));
        }
    }

    @Mod.EventHandler public void startServer(FMLServerStartingEvent event) {
        EVENT_BUS.register(this);
        PermissionAPI.registerNode("gm", DefaultPermissionLevel.OP, "Game Master permission");
    }
}
