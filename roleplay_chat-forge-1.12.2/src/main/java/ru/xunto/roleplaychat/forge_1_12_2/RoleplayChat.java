package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(modid = RoleplayChat.MODID, name = RoleplayChat.NAME, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";


    public static TextFormatting toMinecraftFormatting(TextColor color) {
        for (TextFormatting value : TextFormatting.values()) {
            if (value.name() == color.name()) return value;
        }

        return TextFormatting.WHITE;
    }

    private static ITextComponent coloredComponent(String content, TextColor color) {
        ITextComponent component = new TextComponentString(content);
        component.getStyle().setColor(RoleplayChat.toMinecraftFormatting(color));
        return component;
    }

    public static ITextComponent toTextComponent(Text text) {
        ITextComponent result = RoleplayChat.coloredComponent("", text.getDefaultColor());

        for (TextComponent component : text.getComponents()) {
            result.appendSibling(
                    RoleplayChat.coloredComponent(
                            component.getContent(),
                            component.getColor()
                    )
            );
        }

        return result;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        if (event instanceof CompatServerChatEvent)
            return;

        List<Text> texts = RoleplayChatCore.instance.process(
                new Request(
                        event.getMessage(),
                        new ForgeSpeaker(event.getPlayer()),
                        new ForgeWorld(event.getPlayer().getServerWorld())
                )
        );

        event.setCanceled(true);

        for (Text text : texts) {
            ITextComponent component = RoleplayChat.toTextComponent(text);
            EVENT_BUS.post(
                    new CompatServerChatEvent(event.getPlayer(), component.getUnformattedText(), component)
            );
        }
    }

    @Mod.EventHandler
    public void startServer(FMLServerStartingEvent event) {
        EVENT_BUS.register(this);
        PermissionAPI.registerNode("gm", DefaultPermissionLevel.OP, "Game Master permission");
    }
}
