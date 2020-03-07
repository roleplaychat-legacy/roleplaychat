package ru.xunto.roleplaychat.features.middleware;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.xunto.roleplaychat.ChatTest;
import ru.xunto.roleplaychat.TestUtility;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.features.middleware.distance.Distance;
import ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import static org.testng.Assert.assertEquals;
import static ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware.DISTANCE;
import static ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware.FORCE_ENVIRONMENT;

public class DistanceMiddlewareTest extends ChatTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Middleware instance = new DistanceMiddleware();

    @Test
    public void testDistanceFromMessage() {
        testDistanceFromMessageCase("test", Distance.NORMAL);
        testDistanceFromMessageCase("= test", Distance.QUITE);
        testDistanceFromMessageCase("== test", Distance.WHISPER);
        testDistanceFromMessageCase("=== test", Distance.QUITE_WHISPER);
        testDistanceFromMessageCase("==== test", Distance.QUITE_WHISPER);
        testDistanceFromMessageCase("! test", Distance.LOUD);
        testDistanceFromMessageCase("!! test", Distance.SHOUT);
        testDistanceFromMessageCase("!!! test", Distance.LOUD_SHOUT);
        testDistanceFromMessageCase("!!!! test", Distance.LOUD_SHOUT);

        testDistanceFromMessageCase("!=! test", Distance.LOUD);
        testDistanceFromMessageCase("=!= test", Distance.QUITE);
    }

    private void testDistanceFromMessageCase(String text, Distance distance) {
        Request request = setUpRequest(text);
        Environment environment = setUpEnvironment(text);
        instance.process(request, environment, TestUtility.DO_NOTHING);

        assertEquals(distance, environment.getState().getValue(DistanceMiddleware.DISTANCE));
    }

    @Test
    public void testRecipientHandling() {
        ISpeaker[] players = new ISpeaker[]{
                setUpPlayer(new Position(Distance.QUITE_WHISPER.getDistance(), 0, 0)),
                setUpPlayer(new Position(Distance.QUITE.getDistance(), 0, 0)),
                setUpPlayer(new Position(Distance.NORMAL.getDistance(), 0, 0)),
                setUpPlayer(new Position(Distance.LOUD.getDistance(), 0, 0)),
                setUpPlayer(new Position(Distance.SHOUT.getDistance(), 0, 0)),
                setUpPlayer(new Position(Distance.LOUD_SHOUT.getDistance(), 0, 0))
        };

        Mockito.doReturn(players).when(world).getPlayers();

        testRecipientHandlingCase(Distance.QUITE_WHISPER, 1);
        testRecipientHandlingCase(Distance.QUITE, 2);
        testRecipientHandlingCase(Distance.NORMAL, 3);
        testRecipientHandlingCase(Distance.LOUD, 4);
        testRecipientHandlingCase(Distance.SHOUT, 5);
        testRecipientHandlingCase(Distance.LOUD_SHOUT, 6);
    }

    public void testRecipientHandlingCase(Distance distance, int number) {
        Request request = setUpRequest("");
        Environment environment = setUpEnvironment("");

        environment.getState().setValue(DistanceMiddleware.DISTANCE, distance);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(number, environment.getRecipients().size());
    }

    @Test
    public void ensurePreferInMessageDistance() {
        Mockito.doReturn(new ISpeaker[0]).when(world).getPlayers();

        Request request = setUpRequest("=test");
        Environment environment = setUpEnvironment("=test");
        environment.getState().setValue(DISTANCE, Distance.LOUD_SHOUT);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(Distance.QUITE, environment.getState().getValue(DISTANCE));
    }

    @Test
    public void testForcedEnvironment() {
        Mockito.doReturn(new ISpeaker[0]).when(world).getPlayers();

        Request request = setUpRequest("=test");
        Environment environment = setUpEnvironment("");
        environment.getState().setValue(DISTANCE, Distance.LOUD_SHOUT);
        environment.getState().setValue(FORCE_ENVIRONMENT, true);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(Distance.LOUD_SHOUT, environment.getState().getValue(DISTANCE));
    }

    @Test
    public void testForcedDistance() {
        Mockito.doReturn(new ISpeaker[0]).when(world).getPlayers();

        Request request = setUpRequest("test");
        Environment environment = setUpEnvironment("");
        environment.getState().setValue(DISTANCE, Distance.LOUD_SHOUT);

        instance.process(request, environment, TestUtility.DO_NOTHING);
        assertEquals(environment.getState().getValue(DISTANCE), Distance.LOUD_SHOUT);
    }
}
