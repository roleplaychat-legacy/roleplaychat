package ru.xunto.roleplaychat.forge_1_12_2;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.framework.commands.CommandException;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class ForgeCommand extends CommandBase {
    private ICommand command;

    public ForgeCommand(ICommand command) {
        this.command = command;
    }

    @Override
    public String getName() {
        return this.command.getCommandName();
    }

    @Override
    @ParametersAreNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return String.format("command.%s.usage", command.getCommandName());
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!(sender instanceof EntityPlayerMP)) return false;

        EntityPlayerMP player = (EntityPlayerMP) sender;
        ForgeSpeaker speaker = new ForgeSpeaker(player);

        return command.canExecute(speaker);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws net.minecraft.command.CommandException {
        if (!(sender instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) sender;
        ForgeSpeaker speaker = new ForgeSpeaker(player);

        try {
            command.execute(speaker, args);
        } catch (CommandException e) {
            throw new net.minecraft.command.CommandException(e.getMessage());
        }
    }
}
