package ru.xunto.roleplaychat.forge_1_7_10;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.ServerChatEvent;

public class CompatServerChatEvent extends ServerChatEvent {
    public CompatServerChatEvent(EntityPlayerMP player, String message, IChatComponent component) {
        super(player, message, new ChatComponentTranslation("", component));
    }
}
