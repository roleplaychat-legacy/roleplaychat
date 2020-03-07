package ru.xunto.roleplaychat.features;

import ru.xunto.roleplaychat.features.middleware.distance.Distance;

public class Translations {
    public static final String DISTANCE_SET = "Установлена дальность общения: %s.";
    public static final String DISTANCE_RESET = "Дальность общения сброшена.";

    public static final String ENDPOINT_SET = "Установлен канал общения: %s.";
    public static final String ENDPOINT_RESET = "Канал общения сброшен.";

    public static final String HEARING_ARGUMENT_EXPECTED = "Введите число или никнейм.";
    public static final String HEARING_LESS_ARGUMENT_EXPECTED = "Слишком много аргументов.";
    public static final String HEARING_MODE_CHANGED = "Режим слышимости игрока %s установлен на [%s]";

    public static final String HEARING_MODE_DISTANCE = "Дистанцая:%d блоков";
    public static final String HEARING_MODE_INFINITY = "Полный";
    public static final String HEARING_MODE_DEFAULT = "Обычный";

    public static String stringifyDistance(Distance range) {
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
