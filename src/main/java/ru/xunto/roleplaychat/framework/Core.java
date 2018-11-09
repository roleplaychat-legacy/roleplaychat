package ru.xunto.roleplaychat.framework;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentBase;
import ru.xunto.roleplaychat.features.endpoints.ActionEndpoint;
import ru.xunto.roleplaychat.features.endpoints.DefaultEndpoint;
import ru.xunto.roleplaychat.features.endpoints.OOCEndpoint;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.ToGmMiddleware;
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
        this.register(new DistanceMiddleware());
        this.register(new ToGmMiddleware());

        this.register(new DefaultEndpoint());
        this.register(new OOCEndpoint());
        this.register(new ActionEndpoint());
    }

    public void process(Request request) {
        Environment response = new Environment();
        response.getVariables().put("username", request.getRequester().getName());
        response.getVariables().put("text", request.getText());

        this.process(request, response);
    }

    public void process(Request request, Environment response) {
        for (Middleware middleware : this.middleware) {
            middleware.process(request, response);
        }

        this.send(response);
    }

    public void send(Environment response) {
        TextComponentBase components =
            response.getTemplate().build(response.getVariables(), response.getColors());

        for (EntityPlayer recipient : response.getRecipients()) {
            recipient.sendMessage(components);
        }
    }

    public void register(Middleware newMiddleware) {
        middleware.add(newMiddleware);
        middleware.sort(Comparator.comparing(Middleware::getPriority));
    }
}
