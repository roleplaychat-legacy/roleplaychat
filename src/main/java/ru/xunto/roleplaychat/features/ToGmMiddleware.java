package ru.xunto.roleplaychat.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.Set;

public class ToGmMiddleware extends Middleware {
    @Override public void process(Request request, Environment environment) {
        Set<EntityPlayer> recipients = environment.getRecipients();
        for (EntityPlayer player : request.getWorld().playerEntities) {
            if (PermissionAPI.hasPermission(player, "gm"))
                recipients.add(player);
        }
    }
}
