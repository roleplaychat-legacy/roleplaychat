package ru.xunto.roleplaychat.features.middleware;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashSet;
import java.util.Set;

public class DistanceMiddleware extends Middleware {
    public final static IProperty<Boolean> CANCEL = new Property<>("cancel_distance");
    public final static IProperty<Boolean> FORCE_ENVIRONMENT =
            new Property<>("force_environment_distance");
    public final static IProperty<Distance> DISTANCE = new Property<>("distance");
    private final static Distance DEFAULT_RANGE = Distance.NORMAL;

    public DistanceMiddleware() {
    }

    public static void main(String[] args) {
        System.out.println(countRangeShifts("", '='));
        System.out.println(countRangeShifts("test", '='));
        System.out.println(countRangeShifts("=", '='));
        System.out.println(countRangeShifts("==", '='));
        System.out.println(countRangeShifts("===", '='));
        System.out.println(countRangeShifts("=test", '='));
        System.out.println(countRangeShifts("==test", '='));
        System.out.println(countRangeShifts("===test", '='));
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
        Position position = requester.getPosition();

        Set<ISpeaker> recipients = new HashSet<>();
        for (ISpeaker recipient : world.getPlayers()) {
            if (position.distance(recipient.getPosition()) <= range.getDistance()) {
                recipients.add(recipient);
            }
        }

        return recipients;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Stage getStage() {
        return Stage.PRE;
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

        String label = Translations.stringifyDistance(range);
        if (label != null)
            state.setValue(Environment.LABEL, label);

        state.setValue(Environment.TEXT, text);

        return range;
    }

    @Override
    public void process(Request request, Environment environment, Flow flow) {
        JTwigState state = environment.getState();
        Boolean canceled = state.getValue(CANCEL, false);
        if (canceled)
            return;

        Distance range;
        Boolean forceEnvironment = state.getValue(FORCE_ENVIRONMENT, false);

        if (forceEnvironment)
            range = state.getValue(DISTANCE, DEFAULT_RANGE);
        else
            range = processDistanceState(request, environment);

        Set<ISpeaker> recipients = fetchRecipients(request, environment, range);
        environment.getRecipients().addAll(recipients);

        flow.next();
    }

    public enum Distance {
        QUITE_WHISPER(0, 1), WHISPER(1, 3), QUITE(2, 9), NORMAL(3, 18), LOUD(4, 36), SHOUT(5,
                60), LOUD_SHOUT(6, 80);

        private static final Distance[] VALUES = new Distance[7];

        static {
            for (Distance value : Distance.values()) {
                VALUES[value.index] = value;
            }
        }

        private final int index;
        private final int distance;

        Distance(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }

        public static Distance get(int index) {
            return VALUES[index];
        }

        public Distance shift(int shift) {
            int index = Math.max(Math.min(this.index + shift, VALUES.length - 1), 0);
            return Distance.VALUES[index];
        }

        public int getIndex() {
            return index;
        }

        public int getDistance() {
            return distance;
        }
    }
}
