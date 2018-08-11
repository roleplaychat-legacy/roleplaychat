package ru.xunto.roleplaychat.framework;

import net.minecraft.entity.player.EntityPlayer;
import ru.xunto.roleplaychat.features.*;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Core {
    public final static Core instance = new Core();

    private List<Middleware> middleware = new ArrayList<>();

    private Core() {
        this.registerMiddleware(new DistanceMiddleware());
        this.registerMiddleware(new ToGmMiddleware());

        this.registerMiddleware(new DefaultEndpoint());
        this.registerMiddleware(new OOCEndpoint());
        this.registerMiddleware(new ActionEndpoint());
    }

    public void process(Request request) {
        Environment response = new Environment();
        response.variables.put("username", request.getRequester().getName());
        response.variables.put("text", request.getText());

        this.process(request, response);
    }

    private void process(Request request, Environment response) {
        for (Middleware middleware : this.middleware) {
            middleware.process(request, response);
        }

        for (EntityPlayer recipient : response.getRecipients()) {
            recipient.sendMessage(response.getComponent());
        }
    }

    public void registerMiddleware(Middleware newMiddleware) {
        middleware.add(newMiddleware);
        middleware.sort(Comparator.comparing(Middleware::getPriority));
    }
}
