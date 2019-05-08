package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.xunto.roleplaychat.framework.api.*;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.Set;
import java.util.function.Predicate;

public class DistanceMiddleware extends Middleware {

    public final static IProperty<Distance> DISTANCE = new Property<>("distance");
    private final static Distance DEFAULT_RANGE = Distance.NORMAL;

    public DistanceMiddleware() {
    }

    private static String stringify(Distance range) {
        /* TODO:
                    remove this hardcode; maybe add to localisation
        */
        switch (range) {
            case QUITE_WHISPER:
                return "едва слышно";
            case WHISPER:
                return "очень тихо";
            case QUITE:
                return "тихо";
            case LOUD:
                return "громко";
            case SHOUT:
                return "очень громко";
            case LOUD_SHOUT:
                return "громогласно";
            default:
                return null;
        }
    }

    private static int countRangeShifts(String text, char symbol) {
        int shift = 0;

        if (text.length() < 1) return shift;

        char[] chars = text.toCharArray();
        while (chars[shift] == symbol)
            shift++;

        return shift;
    }

    @Override public void process(Request request, Environment environment) {
        if (!environment.getRecipients().isEmpty())
            return;

        MessageState state = environment.getState();
        String text = request.getText();

        Distance range = state.getValue(DISTANCE);
        if (state.getValue(DISTANCE) == null) {
            /* TODO:
                    remove this hardcode with '!' and '='; maybe add to external config file
            */
            int plus = countRangeShifts(request.getText(), '!');
            int minus = countRangeShifts(request.getText(), '=');
            text = text.substring(minus + plus);

            range = DEFAULT_RANGE.shift(plus - minus);
            state.setValue(DISTANCE, range);
        }

        World world = request.getWorld();
        Vec3d position = request.getRequester().getPositionVector();

        Set<EntityPlayer> recipients = environment.getRecipients();
        for (EntityPlayer recipient : world.getPlayers(EntityPlayer.class, player -> true)) {
            if (position.distanceTo(recipient.getPositionVector()) <= range.getDistance()) {
                recipients.add(recipient);
            }
        }

        String label = stringify(range);
        if (label != null)
            state.setValue(Environment.LABEL, label);

        state.setValue(Environment.TEXT, text);
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
}
