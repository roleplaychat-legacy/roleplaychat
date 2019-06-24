package ru.xunto.roleplaychat.features;

import ru.xunto.roleplaychat.features.middleware.DistanceMiddleware;

public class Translations {
    public static final String DISTANCE_SET = "Distance was set to %s.";
    public static final String DISTANCE_RESET = "Distance was reset.";

    public static final String ENDPOINT_SET = "Endpoint was set to %s.";
    public static final String ENDPOINT_RESET = "Endpoint was reset.";

    public static String stringifyDistance(DistanceMiddleware.Distance range) {
        /* TODO:
                    remove this hardcode; maybe add to localisation
        */
        switch (range) {
            case QUITE_WHISPER:
                return "едва слышно";
            case WHISPER:
                return "очень тихо";
            case QUITE:
                return "тихо";
            case LOUD:
                return "громко";
            case SHOUT:
                return "очень громко";
            case LOUD_SHOUT:
                return "громогласно";
            default:
                return null;
        }
    }
}
