package ru.xunto.roleplaychat.features.middleware.remember;

import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Stage;

public abstract class AbstractRecallMiddleware extends Middleware {
    @Override public Stage getStage() {
        return Stage.PRE;
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
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
}
