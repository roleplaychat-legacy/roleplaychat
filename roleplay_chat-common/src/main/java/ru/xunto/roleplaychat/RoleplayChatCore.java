package ru.xunto.roleplaychat;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.IPermission;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.commands.CommandDistance;
import ru.xunto.roleplaychat.features.commands.CommandListen;
import ru.xunto.roleplaychat.features.endpoints.ActionEndpoint;
import ru.xunto.roleplaychat.features.endpoints.GmActionEndpoint;
import ru.xunto.roleplaychat.features.endpoints.GmOOCEndpoint;
import ru.xunto.roleplaychat.features.endpoints.OOCEndpoint;
import ru.xunto.roleplaychat.features.middleware.distance.Distance;
import ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.distance.ListenMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallDistanceMiddleware;
import ru.xunto.roleplaychat.features.middleware.remember.RecallEndpointMiddleware;
import ru.xunto.roleplaychat.features.permissions.PermissionGM;
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
    private List<IPermission> permissions = new ArrayList<>();

    public RoleplayChatCore() {
        // Middleware
        this.register(new RecallDistanceMiddleware());
        this.register(new RecallEndpointMiddleware());
        this.register(new DistanceMiddleware());
        this.register(new ListenMiddleware());
        this.register(new ActionEndpoint());

        try {
            this.register(new OOCEndpoint(this).registerCommand("o"));
            this.register(new GmOOCEndpoint(this).registerCommand("gmo"));
            this.register(new GmActionEndpoint(this).registerCommand("gmsay"));
        } catch (PrefixMatchEndpoint.EmptyPrefixException e) {
            e.printStackTrace();
        }

        // Commands
        this.register(new CommandListen());
        this.register(new CommandDistance<>(this, "w", Distance.QUITE_WHISPER));
        this.register(new CommandDistance<>(this, "q", Distance.QUITE));
        this.register(new CommandDistance<>(this, "l", Distance.LOUD));
        this.register(new CommandDistance<>(this, "s", Distance.LOUD_SHOUT));

        // Permission
        this.register(PermissionGM.instance);
    }

    private void register(IPermission permission) {
        this.permissions.add(permission);
    }

    public <T extends Middleware> T findMiddleware(Class<T> clazz) {
        for (Middleware middleware1 : middleware) {
            if (middleware1.getClass().equals(clazz)) {
                return (T) middleware1;
            }
        }

        return null;
    }

    public void onPlayerLeave(ISpeaker speaker) {
        ListenMiddleware.resetHearingMode(speaker);
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

    public List<IPermission> getPermissions() {
        return permissions;
    }
}
