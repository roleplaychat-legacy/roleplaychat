package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.xunto.roleplaychat.ChatTest;
import ru.xunto.roleplaychat.TestUtility;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.DISTANCE;
import static ru.xunto.roleplaychat.features.middleware.DistanceMiddleware.Distance;

public class DistanceMiddlewareTest extends ChatTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Middleware instance = new DistanceMiddleware();

    private void testDistanceFromMessageCase(String text, DistanceMiddleware.Distance distance) {
        Request request = setUpRequest(text);
        Environment environment = setUpEnvironment(text);
        instance.process(request, environment, TestUtility.DO_NOTHING);

        assertEquals(distance, environment.getState().getValue(DistanceMiddleware.DISTANCE));
    }

    @Test public void testDistanceFromMessage()  {
        testDistanceFromMessageCase("test", DistanceMiddleware.Distance.NORMAL);
        testDistanceFromMessageCase("= test", DistanceMiddleware.Distance.QUITE);
        testDistanceFromMessageCase("== test", DistanceMiddleware.Distance.WHISPER);
        testDistanceFromMessageCase("=== test", DistanceMiddleware.Distance.QUITE_WHISPER);
        testDistanceFromMessageCase("==== test", DistanceMiddleware.Distance.QUITE_WHISPER);
        testDistanceFromMessageCase("! test", DistanceMiddleware.Distance.LOUD);
        testDistanceFromMessageCase("!! test", DistanceMiddleware.Distance.SHOUT);
        testDistanceFromMessageCase("!!! test", DistanceMiddleware.Distance.LOUD_SHOUT);
        testDistanceFromMessageCase("!!!! test", DistanceMiddleware.Distance.LOUD_SHOUT);

        testDistanceFromMessageCase("!=! test", DistanceMiddleware.Distance.LOUD);
        testDistanceFromMessageCase("=!= test", DistanceMiddleware.Distance.QUITE);
    }

    public void testRecipientHandlingCase(Distance distance, int number) {
        Request request = setUpRequest("");
        Environment environment = setUpEnvironment("");

        environment.getState().setValue(DistanceMiddleware.DISTANCE, distance);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(number, environment.getRecipients().size());
    }

    @Test public void testRecipientHandling() {
        List<EntityPlayer> players = Arrays
            .asList(setUpPlayer(new Vec3d(Distance.QUITE_WHISPER.getDistance(), 0, 0)),
                setUpPlayer(new Vec3d(Distance.QUITE.getDistance(), 0, 0)),
                setUpPlayer(new Vec3d(Distance.NORMAL.getDistance(), 0, 0)),
                setUpPlayer(new Vec3d(Distance.LOUD.getDistance(), 0, 0)),
                setUpPlayer(new Vec3d(Distance.SHOUT.getDistance(), 0, 0)),
                setUpPlayer(new Vec3d(Distance.LOUD_SHOUT.getDistance(), 0, 0)));

        doReturn(players).when(world).getPlayers(eq(EntityPlayer.class), any());

        testRecipientHandlingCase(Distance.QUITE_WHISPER, 1);
        testRecipientHandlingCase(Distance.QUITE, 2);
        testRecipientHandlingCase(Distance.NORMAL, 3);
        testRecipientHandlingCase(Distance.LOUD, 4);
        testRecipientHandlingCase(Distance.SHOUT, 5);
        testRecipientHandlingCase(Distance.LOUD_SHOUT, 6);
    }

    @Test public void ensurePreferInMessageDistance() {
        doReturn(new ArrayList<>()).when(world).getPlayers(eq(EntityPlayer.class), any());

        Request request = setUpRequest("=test");
        Environment environment = setUpEnvironment("");
        environment.getState().setValue(DISTANCE, Distance.LOUD_SHOUT);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(environment.getState().getValue(DISTANCE), Distance.QUITE);
    }

    @Test public void testForcedDistance() {
        doReturn(new ArrayList<>()).when(world).getPlayers(eq(EntityPlayer.class), any());

        Request request = setUpRequest("test");
        Environment environment = setUpEnvironment("");
        environment.getState().setValue(DISTANCE, Distance.LOUD_SHOUT);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(environment.getState().getValue(DISTANCE), Distance.LOUD_SHOUT);
    }
}
