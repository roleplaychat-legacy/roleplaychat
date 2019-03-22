package ru.xunto.roleplaychat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.Before;
import org.mockito.Mock;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ChatTest {
    protected @Mock World world;
    protected EntityPlayer player;

    @Before public void beforeEveryTest() {
        player = setUpPlayer(new Vec3d(0, 0, 0));
    }

    protected EntityPlayer setUpPlayer(Vec3d vec3d) {
        return setUpPlayer("username", vec3d);
    }

    protected EntityPlayer setUpPlayer(String username, Vec3d vec3d) {
        EntityPlayer player = mock(EntityPlayer.class);
        doReturn(vec3d).when(player).getPositionVector();
        doReturn(username).when(player).getName();
        return player;
    }

    protected Request setUpRequest(String text) {
        return new Request(text, player, world);
    }

    protected Environment setUpEnvironment(String text) {
        return new Environment(player.getName(), text);
    }
}
