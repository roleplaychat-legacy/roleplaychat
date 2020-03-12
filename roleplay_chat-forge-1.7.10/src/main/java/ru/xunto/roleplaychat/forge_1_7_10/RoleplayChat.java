package ru.xunto.roleplaychat.forge_1_7_10;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.ICompat;
import ru.xunto.roleplaychat.api.ILogger;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;

import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(modid = RoleplayChat.MODID, version = RoleplayChat.VERSION, acceptableRemoteVersions = "*")
public class RoleplayChat implements ILogger, ICompat {
    public static final String MODID = "@@MODID@@";
    public static final String VERSION = "@@VERSION@@";

    public static EnumChatFormatting toMinecraftFormatting(TextColor color) {
        for (EnumChatFormatting value : EnumChatFormatting.values()) {
            if (value.name().equals(color.name())) return value;
        }

        return EnumChatFormatting.WHITE;
    }

    public static IChatComponent createComponent(String content, TextColor color) {
        IChatComponent component = new ChatComponentText("").appendSibling(ForgeHooks.newChatWithLinks(content));
        component.getChatStyle().setColor(RoleplayChat.toMinecraftFormatting(color));

        return component;
    }

    public static IChatComponent convertComponent(TextComponent component) {
        IChatComponent mcComponent = RoleplayChat.createComponent(
                component.getContent(),
                component.getColor()
        );

        Text hoverText = component.getHoverText();
        if (hoverText != null) {
            mcComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    RoleplayChat.toTextComponent(hoverText)
            ));
        }

        return mcComponent;
    }

    public static IChatComponent toTextComponent(Text text) {
        Object cache = text.getCache();
        if (cache instanceof IChatComponent) return (IChatComponent) cache;

        IChatComponent result = RoleplayChat.createComponent("", text.getDefaultColor());

        for (TextComponent component : text.getComponents()) {
            result.appendSibling(RoleplayChat.convertComponent(component));
        }

        text.setCache(result);
        return result;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ServerChatEvent event) {
        if (event instanceof CompatServerChatEvent)
            return;

        List<Text> texts = RoleplayChatCore.instance.process(
                new Request(event.message, new ForgeSpeaker(event.player))
        );

        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        RoleplayChatCore.instance.onPlayerLeave(new ForgeSpeaker((EntityPlayerMP) event.player));
    }

    @Mod.EventHandler
    public void startServer(FMLServerStartingEvent event) {
        EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        RoleplayChatCore.instance.warmUpRenderer();
        RoleplayChatCore.instance.setLogger(this);
        RoleplayChatCore.instance.registerCompat(this);

        MinecraftServer server = MinecraftServer.getServer();
        ServerCommandManager manager = (ServerCommandManager) server.getCommandManager();

        for (ICommand command : RoleplayChatCore.instance.getCommands()) {
            manager.registerCommand(new ForgeCommand(command));
        }
    }

    public boolean compat(ISpeaker speaker, Text text) {
        EntityPlayerMP mcPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(speaker.getRealName());

        if (mcPlayer == null) return false;

        IChatComponent component = RoleplayChat.toTextComponent(text);
        return EVENT_BUS.post(
                new CompatServerChatEvent(mcPlayer, component.getUnformattedText(), component)
        );
    }

    @Override
    public void log(Text text) {
        IChatComponent component = RoleplayChat.toTextComponent(text);
        FMLCommonHandler.instance().getMinecraftServerInstance().addChatMessage(component);
    }
}
