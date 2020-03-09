package ru.xunto.roleplaychat.spigot;

import org.bukkit.ChatColor;
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

        SpigotSpeaker speaker = new SpigotSpeaker((Player) commandSender);

        if (!this.command.canExecute(speaker)) {
            commandSender.sendMessage(ChatColor.RED + "You cannot run this command.");
            return false;
        }

        try {
            this.command.execute(speaker, strings);
        } catch (CommandException e) {
            commandSender.sendMessage(e.getMessage());
            return false;
        }
        return true;
    }
}
