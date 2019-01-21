package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.xunto.roleplaychat.framework.api.*;

import java.util.Map;
import java.util.Set;

public class DistanceMiddleware extends Middleware {
    public final static DistanceMiddleware INSTANCE = new DistanceMiddleware();
    private final static Distance DEFAULT_RANGE = Distance.NORMAL;

    private static String stringify(Distance range) {
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

        char[] chars = text.toCharArray();
        while (chars[shift] == symbol)
            shift++;

        return shift;
    }

    public Environment markDistance(Environment environment, Distance distance) {
        environment.getVariables().put("distance", Integer.toString(distance.getIndex()));
        return environment;
    }

    @Override public void process(Request request, Environment environment) {
        if (!environment.getRecipients().isEmpty())
            return;

        Distance range;
        String text = request.getText();
        if (environment.getVariables().containsKey("distance")) {
            String range_string = environment.getVariables().get("distance");
            range = Distance.get(Integer.valueOf(range_string));
        } else {
            int plus = DistanceMiddleware.countRangeShifts(request.getText(), '!');
            int minus = DistanceMiddleware.countRangeShifts(request.getText(), '=');
            range = DEFAULT_RANGE.shift(plus - minus);
            text = text.substring(minus + plus);
        }

        World world = request.getWorld();
        Vec3d position = request.getRequester().getPositionVector();

        Set<EntityPlayer> recipients = environment.getRecipients();
        for (EntityPlayer recipient : world.playerEntities) {
            if (position.distanceTo(recipient.getPositionVector()) <= range.getDistance()) {
                recipients.add(recipient);
            }
        }

        Map<String, String> variables = environment.getVariables();

        String label = stringify(range);
        if (label != null)
            variables.put("label", "(" + label + ")");

        variables.put("text", text);
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
