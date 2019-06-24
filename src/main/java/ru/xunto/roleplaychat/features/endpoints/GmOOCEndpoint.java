package ru.xunto.roleplaychat.features.endpoints;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - EntityPlayer
            - TextFormatting
            - World
            - PermissionAPI
*/

public class GmOOCEndpoint extends PrefixMatchEndpoint {
    private static final Map<String, TextFormatting> colors = new HashMap<>();

    static {
        colors.put("default", TextFormatting.GOLD);
        colors.put("username", TextFormatting.GREEN);
        colors.put("label", TextFormatting.WHITE);
    }

    private JTwigTemplate template = new JTwigTemplate("templates/gm_ooc.twig");

    public GmOOCEndpoint() throws EmptyPrefixException {
        super("-");
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        environment.setTemplate(template);
        environment.getColors().putAll(colors);

        Set<EntityPlayer> recipients = environment.getRecipients();
        recipients.clear();

        World[] worlds = Objects.requireNonNull(request.getWorld().getMinecraftServer()).worlds;
        for (World world : worlds) {
            for (EntityPlayer player : world.playerEntities) {
                if (PermissionAPI.hasPermission(player, "gm"))
                    recipients.add(player);
            }
        }

        recipients.add(request.getRequester());
    }
}
