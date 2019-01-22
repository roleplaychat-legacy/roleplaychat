package ru.xunto.roleplaychat.framework;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import ru.xunto.roleplaychat.features.endpoints.*;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.ToGmMiddleware;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.state.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Core {
    public final static StringProperty USERNAME = new StringProperty("username");
    public final static StringProperty TEXT = new StringProperty("text");

    private List<Middleware> middleware = new ArrayList<>();

    public Core() {
        this.register(DistanceMiddleware.INSTANCE);
        this.register(new ToGmMiddleware());

        this.register(new DefaultEndpoint());
        this.register(new OOCEndpoint());
        this.register(new ActionEndpoint());
        this.register(new GmOOCEndpoint());
        this.register(new GmActionEndpoint());
    }

    public ITextComponent process(Request request) {
        Environment response = new Environment();
        response.getState().setValue(USERNAME, request.getRequester().getName());
        response.getState().setValue(TEXT, request.getText());

        return this.process(request, response);
    }

    public ITextComponent process(Request request, Environment environment) {
        environment.setCore(this);

        for (Middleware middleware : this.middleware) {
            middleware.process(request, environment);
        }

        return this.send(environment);
    }

    public ITextComponent send(Environment environment) {
        TextComponentBase components =
            environment.getTemplate().build(environment.getState(), environment.getColors());

        for (EntityPlayer recipient : environment.getRecipients()) {
            recipient.sendMessage(components);
        }

        return components;
    }

    public void register(Middleware newMiddleware) {
        middleware.add(newMiddleware);

        middleware.sort((o1, o2) -> {
            int compare = o1.getStage().compareTo(o2.getStage());
            if (compare != 0)
                return compare;

            return o1.getPriority().compareTo(o2.getPriority());
        });
    }
}
