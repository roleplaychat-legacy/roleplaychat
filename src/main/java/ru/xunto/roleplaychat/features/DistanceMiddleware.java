package ru.xunto.roleplaychat.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Priority;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.Set;

public class DistanceMiddleware extends Middleware {
    private final static int[] RANGES = {1, 3, 9, 18, 36, 60, 80};

    private final static int DEFAULT_SHIFT = 3;
    private final static int MAX_RANGE_SHIFT = 3;

    private static int countRangeShifts(String text, char symbol) {
        int shift = 0;

        char[] chars = text.toCharArray();
        while (chars[shift] == symbol)
            shift++;

        return shift > MAX_RANGE_SHIFT ? MAX_RANGE_SHIFT : shift;
    }

    @Override public void process(Request request, Environment environment) {
        if (!environment.getRecipients().isEmpty())
            return;

        int plus = DistanceMiddleware.countRangeShifts(request.getText(), '!');
        int minus = DistanceMiddleware.countRangeShifts(request.getText(), '=');

        int shift = DEFAULT_SHIFT + plus - minus;

        World world = request.getWorld();
        Vec3d position = request.getRequester().getPositionVector();

        Set<EntityPlayer> recipients = environment.getRecipients();
        for (EntityPlayer recipient : world.playerEntities) {
            if (position.distanceTo(recipient.getPositionVector()) <= RANGES[shift]) {
                recipients.add(recipient);
            }
        }

        environment.getVariables().put("text", request.getText().substring(minus + plus));
    }

    @Override public Priority getPriority() {
        return Priority.HIGH;
    }
}
