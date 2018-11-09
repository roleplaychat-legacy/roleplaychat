package ru.xunto.roleplaychat.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.Objects;
import java.util.Set;

public class ToGmMiddleware extends Middleware {
    @Override public void process(Request request, Environment environment) {
        Set<EntityPlayer> recipients = environment.getRecipients();

        World[] worlds = Objects.requireNonNull(request.getWorld().getMinecraftServer()).worlds;

        for (World world : worlds) {
            for (EntityPlayer player : world.playerEntities) {
                if (PermissionAPI.hasPermission(player, "gm"))
                    recipients.add(player);
            }
        }
    }
}
