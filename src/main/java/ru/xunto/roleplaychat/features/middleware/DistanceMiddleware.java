package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.xunto.roleplaychat.features.Translations;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashSet;
import java.util.Set;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - EntityPlayer
            - World
            - Vec3d
 */


public class DistanceMiddleware extends Middleware {
    public final static IProperty<Boolean> CANCEL = new Property<>("cancel_distance");
    public final static IProperty<Distance> DISTANCE = new Property<>("distance");
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

    @Override public void process(Request request, Environment environment, Runnable next) {
        JTwigState state = environment.getState();
        Boolean canceled = state.getValue(CANCEL);
        if (canceled != null && canceled)
            return;

        Distance range = state.getValue(DISTANCE);
        if (range == null)
            range = processDistanceState(request, environment);
        Set<EntityPlayer> recipients = fetchRecipients(request, environment, range);

        environment.getRecipients().addAll(recipients);

        next.run();
    }

    public static Set<EntityPlayer> fetchRecipients(Request request, Environment environment,
        Distance range) {
        World world = request.getWorld();
        Vec3d position = request.getRequester().getPositionVector();

        Set<EntityPlayer> recipients = new HashSet<>();
        for (EntityPlayer recipient : world.getPlayers(EntityPlayer.class, player -> true)) {
            if (position.distanceTo(recipient.getPositionVector()) <= range.getDistance()) {
                recipients.add(recipient);
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

        String label = Translations.stringifyDistance(range);
        if (label != null)
            state.setValue(Environment.LABEL, label);

        state.setValue(Environment.TEXT, text);

        return range;
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
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
}
