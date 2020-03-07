package ru.xunto.roleplaychat.features.middleware.distance.hearing_gm;

import ru.xunto.roleplaychat.api.ISpeaker;

public interface IHearingMode {
    boolean canAvoidHearingRestriction(ISpeaker recipient, ISpeaker speaker);

    String getHumanReadable();
}
