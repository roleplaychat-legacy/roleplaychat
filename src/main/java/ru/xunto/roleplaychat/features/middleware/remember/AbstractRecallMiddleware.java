package ru.xunto.roleplaychat.features.middleware.remember;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Stage;

public abstract class AbstractRecallMiddleware extends Middleware {
    static void sendSetMessage(EntityPlayer requester, String text) {
        TextComponentString component = new TextComponentString(text);
        component.getStyle().setColor(TextFormatting.GREEN);
        requester.sendMessage(component);
    }

    static boolean isSetRequest(String text, String[] prefixes) {
        boolean controls = false;
        for (String prefix : prefixes) {
            controls |= text.startsWith(prefix);
        }

        for (String prefix : prefixes) {
            text = text.replace(prefix, "");
        }

        return controls && text.isEmpty();
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
    }
}
