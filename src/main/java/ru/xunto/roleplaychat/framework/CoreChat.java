package ru.xunto.roleplaychat.framework;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.xunto.roleplaychat.features.endpoints.*;
import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.AbstractRecallMiddleware;
import ru.xunto.roleplaychat.features.middleware.ToGmMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallDistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallEndpointMiddleware;
import ru.xunto.roleplaychat.framework.api.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - EntityPlayer
            - TextFormatting
*/

public class CoreChat {
    private List<Middleware> middleware = new ArrayList<>();

    public CoreChat() {
        this.register(new RecallDistanceMiddleware());
        this.register(new RecallEndpointMiddleware());
        this.register(new DistanceMiddleware());
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

    public ITextComponent process(Request request) {
        Environment response = new Environment(request.getRequester().getName(), request.getText());

        return this.process(request, response);
    }

    public ITextComponent process(Request request, Environment environment) {
        environment.setCore(this);

        for (Middleware middleware : this.middleware) {
            middleware.process(request, environment);
            if (environment.isInterrupted()) return null;
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

        middleware.sort(Comparator.comparing(Middleware::getStage).thenComparing(Middleware::getPriority));
    }

    public List<Middleware> getMiddleware() {
        return middleware;
    }

    // Initialize the JTwig in advance 'cause there may be freezes
    private void warmUpRenderer() {
        JtwigTemplate.inlineTemplate("warm up").render(new JtwigModel());
    }
}
