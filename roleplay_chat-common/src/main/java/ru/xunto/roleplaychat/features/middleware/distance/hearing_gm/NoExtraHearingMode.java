package ru.xunto.roleplaychat.features.middleware.distance.hearing_gm;

import ru.xunto.roleplaychat.api.ISpeaker;

public class NoExtraHearingMode implements IHearingMode {
    public static NoExtraHearingMode instance = new NoExtraHearingMode();

    @Override
    public boolean canAvoidHearingRestriction(ISpeaker recipient, ISpeaker speaker) {
        return false;
    }

    @Override
    public String getHumanReadable() {
        return "default";
    }
}
