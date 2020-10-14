package ru.xunto.roleplaychat.forge_1_12_2;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.Objects;
import java.util.UUID;

public class ForgeEntitySpeaker implements ISpeaker {
    private final Entity entity;

    public ForgeEntitySpeaker(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void sendMessage(String text, TextColor color) {
        entity.sendMessage(RoleplayChat.createComponent(text, color));
    }

    @Override
    public void sendMessage(Text text) {
        entity.sendMessage(RoleplayChat.toTextComponent(text));
    }

    @Override
    public String getName() {
        return entity.getDisplayName().getFormattedText();
    }

    @Override
    public String getRealName() {
        return entity.getName();
    }

    @Override
    public Position getPosition() {
        BlockPos position = entity.getPosition();
        return new Position(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public IWorld getWorld() {
        return new ForgeWorld((WorldServer) entity.getEntityWorld());
    }

    @Override
    public UUID getUniqueID() {
        return entity.getUniqueID();
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForgeEntitySpeaker)) return false;
        ForgeEntitySpeaker that = (ForgeEntitySpeaker) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity);
    }
}
