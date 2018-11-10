package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.xunto.roleplaychat.features.Distance;
import ru.xunto.roleplaychat.framework.api.*;

import java.util.Map;
import java.util.Set;

public class DistanceMiddleware extends Middleware {
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

    @Override public void process(Request request, Environment environment) {
        if (!environment.getRecipients().isEmpty())
            return;

        int plus = DistanceMiddleware.countRangeShifts(request.getText(), '!');
        int minus = DistanceMiddleware.countRangeShifts(request.getText(), '=');

        Distance range = DEFAULT_RANGE.shift(plus - minus);

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

        variables.put("text", request.getText().substring(minus + plus));
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override public Stage getStage() {
        return Stage.PRE;
    }

}
