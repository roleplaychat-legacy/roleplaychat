package ru.xunto.roleplaychat;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.commands.HearingCommand;
import ru.xunto.roleplaychat.features.endpoints.ActionEndpoint;
import ru.xunto.roleplaychat.features.endpoints.GmActionEndpoint;
import ru.xunto.roleplaychat.features.endpoints.GmOOCEndpoint;
import ru.xunto.roleplaychat.features.endpoints.OOCEndpoint;
import ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.distance.ToGmMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallDistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallEndpointMiddleware;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.renderer.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoleplayChatCore {
    public final static RoleplayChatCore instance = new RoleplayChatCore();

    private List<Middleware> middleware = new ArrayList<>();
    private List<ICommand> commands = new ArrayList<>();

    public RoleplayChatCore() {
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

        this.register(new HearingCommand());
    }

    public void onPlayerLeave(ISpeaker speaker) {
        ToGmMiddleware.resetHearingMode(speaker);
    }

    public void register(Middleware newMiddleware) {
        middleware.add(newMiddleware);
        middleware.sort(Comparator.comparing(Middleware::getStage).thenComparing(Middleware::getPriority));
    }

    public void register(ICommand command) {
        commands.add(command);
    }

    // Initialize the JTwig in advance 'cause there may be freezes
    public void warmUpRenderer() {
        JtwigTemplate.inlineTemplate("warm up").render(new JtwigModel());
    }

    public List<Text> process(Request request) {
        Environment response = new Environment(request.getRequester().getName(), request.getText());

        return this.process(request, response);
    }

    public List<Text> process(Request request, Environment environment) {
        environment.setCore(this);

        List<Environment> toSend = new ArrayList<>();
        Flow flow = new Flow(this.middleware, request, environment, toSend::add);
        flow.next();

        List<Text> result = new ArrayList<>();
        for (Environment resultEnvironment : toSend) {
            Text text = this.send(resultEnvironment);

            if (!resultEnvironment.getState().getValue(Flow.IS_LIGHT_FORK, false)) {
                result.add(text);
            }
        }

        return result;
    }

    public Text send(Environment environment) {
        Text text = environment.getTemplate().render(environment.getState(), environment.getColors());

        for (ISpeaker recipient : environment.getRecipients()) {
            recipient.sendMessage(text);
        }

        return text;
    }

    public List<Middleware> getMiddleware() {
        return middleware;
    }

    public List<ICommand> getCommands() {
        return this.commands;
    }
}
