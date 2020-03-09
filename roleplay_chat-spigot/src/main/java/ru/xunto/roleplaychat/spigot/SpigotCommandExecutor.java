package ru.xunto.roleplaychat.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.framework.commands.CommandException;

public class SpigotCommandExecutor implements CommandExecutor {
    private ICommand command;

    public SpigotCommandExecutor(ICommand command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        try {
            this.command.execute(
                    new SpigotSpeaker((Player) commandSender),
                    strings
            );
        } catch (CommandException e) {
            e.printStackTrace();
        }
        return false;
    }
}
