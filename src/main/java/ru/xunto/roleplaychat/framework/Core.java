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
    public final static Core instance = new Core();

    public final static StringProperty USERNAME = new StringProperty("username");
    public final static StringProperty TEXT = new StringProperty("text");

    private List<Middleware> middleware = new ArrayList<>();

    private Core() {
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

    public ITextComponent process(Request request, Environment response) {
        for (Middleware middleware : this.middleware) {
            middleware.process(request, response);
        }

        return this.send(response);
    }

    public ITextComponent send(Environment response) {
        TextComponentBase components =
            response.getTemplate().build(response.getState(), response.getColors());

        for (EntityPlayer recipient : response.getRecipients()) {
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
