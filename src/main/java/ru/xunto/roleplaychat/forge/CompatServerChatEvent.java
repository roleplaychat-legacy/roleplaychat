package ru.xunto.roleplaychat.forge;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.ServerChatEvent;

public class CompatServerChatEvent extends ServerChatEvent {
    public CompatServerChatEvent(EntityPlayerMP player, String message, ITextComponent component) {
        super(player, message, component);
    }
}
