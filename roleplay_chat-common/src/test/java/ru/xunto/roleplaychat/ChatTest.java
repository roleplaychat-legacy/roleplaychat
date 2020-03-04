package ru.xunto.roleplaychat;

import org.junit.Before;
import org.mockito.Mockito;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;

public class ChatTest {
    protected IServer server;
    protected IWorld world;
    protected ISpeaker player;

    @Before
    public void beforeEveryTest() {
        server = Mockito.mock(IServer.class);

        player = setUpPlayer(new Position(0, 0, 0));
        world = setUpWorld(server, player);

        Mockito.doReturn(new IWorld[]{world}).when(server).getWorlds();
    }

    private IWorld setUpWorld(IServer server, ISpeaker... player) {
        IWorld world = Mockito.mock(IWorld.class);
        Mockito.doReturn(player).when(world).getPlayers();
        Mockito.doReturn(server).when(world).getServer();

        return world;
    }

    protected ISpeaker setUpPlayer(Position vec3d) {
        return setUpPlayer("username", vec3d);
    }

    protected ISpeaker setUpPlayer(String username, Position position) {
        ISpeaker player = Mockito.mock(ISpeaker.class);
        Mockito.doReturn(position).when(player).getPosition();
        Mockito.doReturn(username).when(player).getName();

        return player;
    }

    protected Request setUpRequest(String text) {
        return new Request(text, player, world);
    }

    protected Environment setUpEnvironment(String text) {
        return new Environment(player.getName(), text);
    }
}
