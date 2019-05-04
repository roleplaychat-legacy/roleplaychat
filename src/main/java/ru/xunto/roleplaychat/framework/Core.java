package ru.xunto.roleplaychat.framework;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.xunto.roleplaychat.features.endpoints.*;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.ToGmMiddleware;
import ru.xunto.roleplaychat.framework.api.ChatException;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.ArrayList;
import java.util.List;

public class Core {

    private List<Middleware> middleware = new ArrayList<>();

    public Core() {
        this.register(DistanceMiddleware.INSTANCE);
        this.register(new ToGmMiddleware());
        this.register(new ActionEndpoint());

        try {
            this.register(new OOCEndpoint());
            this.register(new GmOOCEndpoint());
            this.register(new GmActionEndpoint());
        } catch (PrefixMatchEndpoint.EmptyPrefixException e) {
            e.printStackTrace();
        }

        this.warmUpRenderer();
    }

    public ITextComponent process(Request request) throws ChatException {
        Environment response = new Environment(request.getRequester().getName(), request.getText());

        return this.process(request, response);
    }

    public ITextComponent process(Request request, Environment environment) throws ChatException {
        environment.setCore(this);

        for (Middleware middleware : this.middleware) {
            middleware.process(request, environment);
        }

        return this.send(environment);
    }

    public ITextComponent send(Environment environment) {
        ITextComponent components =
            environment.getTemplate().render(environment.getState(), environment.getColors());

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

    public void warmUpRenderer() {
        JtwigTemplate.inlineTemplate("warm up").render(new JtwigModel());
    }
}
