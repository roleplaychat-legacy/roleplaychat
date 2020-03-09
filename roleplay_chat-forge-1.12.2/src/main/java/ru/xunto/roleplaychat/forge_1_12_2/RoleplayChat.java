package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.IPermission;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(modid = RoleplayChat.MODID, name = RoleplayChat.NAME, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat {
    public static final String MODID = "@@MODID@@";
    public static final String NAME = "@@MODID@@";
    public static final String VERSION = "@@VERSION@@";

    public static TextFormatting toMinecraftFormatting(TextColor color) {
        for (TextFormatting value : TextFormatting.values()) {
            if (value.name().equals(color.name())) return value;
        }

        return TextFormatting.WHITE;
    }

    public static ITextComponent createComponent(String content, TextColor color) {
        ITextComponent component = new TextComponentString("").appendSibling(ForgeHooks.newChatWithLinks(content));
        component.getStyle().setColor(RoleplayChat.toMinecraftFormatting(color));

        return component;
    }

    public static ITextComponent toTextComponent(Text text) {
        Object cache = text.getCache();
        if (cache instanceof ITextComponent) return (ITextComponent) cache;

        ITextComponent result = RoleplayChat.createComponent("", text.getDefaultColor());

        for (TextComponent component : text.getComponents()) {
            result.appendSibling(
                    RoleplayChat.createComponent(
                            component.getContent(),
                            component.getColor()
                    )
            );
        }

        text.setCache(result);
        return result;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        if (event instanceof CompatServerChatEvent)
            return;

        List<Text> texts = RoleplayChatCore.instance.process(
                new Request(event.getMessage(), new ForgeSpeaker(event.getPlayer()))
        );

        event.setCanceled(true);

        for (Text text : texts) {
            ITextComponent component = RoleplayChat.toTextComponent(text);
            boolean isCanceled = EVENT_BUS.post(
                    new CompatServerChatEvent(event.getPlayer(), component.getUnformattedText(), component)
            );

            if (!isCanceled) FMLCommonHandler.instance().getMinecraftServerInstance().sendMessage(component);
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        RoleplayChatCore.instance.onPlayerLeave(new ForgeSpeaker((EntityPlayerMP) event.player));
    }

    @Mod.EventHandler
    public void startServer(FMLServerStartingEvent event) {
        EVENT_BUS.register(this);
        RoleplayChatCore.instance.warmUpRenderer();


        for (IPermission permission : RoleplayChatCore.instance.getPermissions()) {
            PermissionAPI.registerNode(
                    permission.getName(),
                    DefaultPermissionLevel.OP,
                    permission.getDescription()
            );
        }

        for (ICommand command : RoleplayChatCore.instance.getCommands()) {
            event.registerServerCommand(new ForgeCommand(command));
        }
    }
}
