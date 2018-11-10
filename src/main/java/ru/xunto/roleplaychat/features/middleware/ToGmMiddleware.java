package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.Core;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.api.Stage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ToGmMiddleware extends Middleware {
    @Override public void process(Request request, Environment environment) {
        Environment newEnvironment = environment.clone();

        Map<String, TextFormatting> colors = newEnvironment.getColors();

        if (colors.get("default") == null) {
            newEnvironment.getColors().put("default", TextFormatting.DARK_GRAY);
        }

        Set<EntityPlayer> originalRecipients = environment.getRecipients();
        Set<EntityPlayer> recipients = newEnvironment.getRecipients();
        recipients.clear();

        World[] worlds = Objects.requireNonNull(request.getWorld().getMinecraftServer()).worlds;

        for (World world : worlds) {
            for (EntityPlayer player : world.playerEntities) {
                boolean allowed = PermissionAPI.hasPermission(player, "gm");
                if (allowed && !originalRecipients.contains(player))
                    recipients.add(player);
            }
        }

        Core.instance.send(newEnvironment);
    }

    @Override public Stage getStage() {
        return Stage.POST;
    }
}
