package ru.xunto.roleplaychat.features.middleware.distance.hearing_gm;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.Translations;

public class DistanceHearingMode implements IHearingMode {
    private int distance;

    public DistanceHearingMode(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean canAvoidHearingRestriction(ISpeaker recipient, ISpeaker speaker) {
        return speaker.getPosition().distance(recipient.getPosition()) <= distance;
    }

    @Override
    public String getHumanReadable() {
        return String.format(Translations.HEARING_MODE_DISTANCE, distance);
    }
}
