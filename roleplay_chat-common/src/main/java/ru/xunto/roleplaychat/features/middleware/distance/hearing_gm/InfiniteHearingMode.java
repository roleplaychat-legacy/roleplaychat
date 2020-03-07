package ru.xunto.roleplaychat.features.middleware.distance.hearing_gm;

import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.Translations;

public class InfiniteHearingMode implements IHearingMode {
    public static InfiniteHearingMode instance = new InfiniteHearingMode();

    @Override
    public boolean canAvoidHearingRestriction(ISpeaker recipient, ISpeaker speaker) {
        return true;
    }

    @Override
    public String getHumanReadable() {
        return Translations.HEARING_MODE_INFINITY;
    }
}
