package ru.xunto.roleplaychat.features.commands;

import org.apache.commons.lang3.ArrayUtils;
import ru.xunto.roleplaychat.api.ICommand;
import ru.xunto.roleplaychat.api.IServer;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.middleware.distance.ToGmMiddleware;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.DistanceHearingMode;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.IHearingMode;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.InfiniteHearingMode;
import ru.xunto.roleplaychat.features.middleware.distance.hearing_gm.NoExtraHearingMode;
import ru.xunto.roleplaychat.framework.commands.CommandException;
import ru.xunto.roleplaychat.framework.commands.CommandUtils;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class HearingCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "hearing";
    }

    @Override
    public String[] getTabCompletion(ISpeaker speaker, String[] args) {
        return CommandUtils.getPlayerNames(speaker.getWorld().getServer());
    }

    @Override
    public boolean canExecute(ISpeaker speaker) {
        return speaker.hasPermission("gm");
    }

    public DistanceHearingMode parseDistance(String string) throws CommandException {
        try {
            int distance = Integer.parseInt(string);
            return new DistanceHearingMode(distance);
        } catch (NumberFormatException e) {
            throw new CommandException("Number expected");
        }
    }

    @Override
    public void execute(ISpeaker speaker, String[] args) throws CommandException {
        ISpeaker target = speaker;
        IServer server = speaker.getWorld().getServer();

        if (args.length > 0) {
            String tryUsername = args[0];
            ISpeaker[] players = CommandUtils.getPlayers(server);

            Optional<ISpeaker> search = Arrays.stream(players).filter((v) -> v.getName().equals(tryUsername)).findFirst();

            if (search.isPresent()) {
                target = search.get();
                args = ArrayUtils.subarray(args, 1, args.length);
            }
        }

        UUID targetUUID = target.getUniqueID();
        IHearingMode hearingMode = ToGmMiddleware.getHearingMode(target);
        IHearingMode newMode;

        if (args.length == 0) {
            if (hearingMode == NoExtraHearingMode.instance) newMode = InfiniteHearingMode.instance;
            else newMode = NoExtraHearingMode.instance;
        } else if (args.length == 1) {
            newMode = this.parseDistance(args[0]);
        } else {
            throw new CommandException("Too much arguments");
        }

        ToGmMiddleware.setHearingMode(target, newMode);
        speaker.sendMessage(String.format(
                "%s's hearing mode changed to %s",
                target.getName(),
                newMode.getHumanReadable()
        ), TextColor.GREEN);
    }

}
