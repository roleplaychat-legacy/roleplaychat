package ru.xunto.roleplaychat.features.middleware.distance;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistanceMiddleware extends Middleware {
    public final static IProperty<Boolean> CANCEL = new Property<>("cancel_distance");
    public final static IProperty<Boolean> FORCE_ENVIRONMENT =
            new Property<>("force_environment_distance");
    public final static IProperty<Distance> DISTANCE = new Property<>("distance");
    public final static IProperty<List<ISpeaker>> ORIGINS = new Property<>("origins");
    private final static Distance DEFAULT_RANGE = Distance.NORMAL;

    public DistanceMiddleware() {
    }

    private static int countRangeShifts(String text, char symbol) {
        int shift = 0;

        char[] chars = text.toCharArray();
        while (shift < text.length() && chars[shift] == symbol)
            shift++;

        return shift;
    }

    public static Set<ISpeaker> fetchRecipients(Request request, Environment environment,
                                                Distance range) {
        ISpeaker requester = request.getRequester();
        IWorld world = requester.getWorld();

        List<ISpeaker> origins = environment.getState().getValue(ORIGINS, null);
        if (origins == null) origins = Collections.singletonList(requester);

        Set<ISpeaker> recipients = new HashSet<>();
        for (ISpeaker origin : origins) {
            for (ISpeaker recipient : world.getPlayers()) {
                if (origin.getPosition().distance(recipient.getPosition()) <= range.getDistance()) {
                    recipients.add(recipient);
                }
            }
        }

        return recipients;
    }

    public static Distance processDistanceState(Request request, Environment environment) {
        MessageState state = environment.getState();
        String text = state.getValue(Environment.TEXT);

        Distance range = state.getValue(DISTANCE);

        int plus = countRangeShifts(text, '!');
        int minus = countRangeShifts(text, '=');

        if (plus - minus != 0 || range == null) {
            text = text.substring(minus + plus);
            range = DEFAULT_RANGE.shift(plus - minus);
        }

        state.setValue(DISTANCE, range);
        state.setValue(Environment.TEXT, text);

        return range;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Stage getStage() {
        return Stage.PRE;
    }

    @Override
    public void process(Request request, Environment environment, Flow flow) {
        MessageState state = environment.getState();
        Boolean canceled = state.getValue(CANCEL, false);
        if (canceled)
            return;

        Distance distance;
        Boolean forceEnvironment = state.getValue(FORCE_ENVIRONMENT, false);

        // TODO: Improve forced environment behaviour to be clearer
        if (forceEnvironment)
            distance = state.getValue(DISTANCE, DEFAULT_RANGE);
        else
            distance = processDistanceState(request, environment);

        String label = Translations.stringifyDistance(distance);
        if (label != null) state.setValue(Environment.LABEL, label);

        Set<ISpeaker> recipients = fetchRecipients(request, environment, distance);
        environment.getRecipients().addAll(recipients);

        flow.next();
    }

}
