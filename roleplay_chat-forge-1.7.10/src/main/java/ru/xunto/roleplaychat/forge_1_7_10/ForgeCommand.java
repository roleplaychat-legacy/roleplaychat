package ru.xunto.roleplaychat.forge_1_7_10;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.framework.commands.CommandException;

public class ForgeCommand extends CommandBase {
    private ICommand command;

    public ForgeCommand(ICommand command) {
        this.command = command;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (!(sender instanceof EntityPlayerMP)) return false;

        EntityPlayerMP player = (EntityPlayerMP) sender;
        ForgeSpeaker speaker = new ForgeSpeaker(player);

        return command.canExecute(speaker);
    }

    @Override
    public String getCommandName() {
        return command.getCommandName();
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return String.format("command.%s.usage", command.getCommandName());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
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
