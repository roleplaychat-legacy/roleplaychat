package ru.xunto.roleplaychat.features.middleware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Middleware;
import ru.xunto.roleplaychat.framework.api.Request;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DistanceMiddlewareTest {
    @Mock EntityPlayer player;
    @Mock World world;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Middleware instance = DistanceMiddleware.INSTANCE;

    private Request setUpRequest(String text) {
        return new Request(text, player, world);
    }

    private Environment setUpEnvironment(String text) {
        return new Environment("username", "text");
    }

    private void testLabel(String text, DistanceMiddleware.Distance distance) {
        Request request = setUpRequest(text);
        Environment environment = setUpEnvironment(text);
        instance.process(request, environment);

        assertEquals(environment.getState().getValue(DistanceMiddleware.DISTANCE), distance);
    }

    @Test public void testLabel() {
        testLabel("test", DistanceMiddleware.Distance.NORMAL);
        testLabel("= test", DistanceMiddleware.Distance.QUITE);
        testLabel("== test", DistanceMiddleware.Distance.WHISPER);
        testLabel("=== test", DistanceMiddleware.Distance.QUITE_WHISPER);
        testLabel("==== test", DistanceMiddleware.Distance.QUITE_WHISPER);
    }
}
