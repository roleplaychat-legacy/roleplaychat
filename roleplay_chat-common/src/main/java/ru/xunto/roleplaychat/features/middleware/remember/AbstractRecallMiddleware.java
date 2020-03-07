package ru.xunto.roleplaychat.features.middleware.remember;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Stage;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

public abstract class AbstractRecallMiddleware extends Middleware {
    static void sendSetMessage(ISpeaker requester, String text) {
        requester.sendMessage(text, TextColor.GREEN);
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

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Stage getStage() {
        return Stage.PRE;
    }
}
