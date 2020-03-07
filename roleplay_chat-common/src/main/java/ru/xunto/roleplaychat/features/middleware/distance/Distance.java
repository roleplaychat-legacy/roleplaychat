package ru.xunto.roleplaychat.features.middleware.distance;

public enum Distance {
    QUITE_WHISPER(0, 1), WHISPER(1, 3), QUITE(2, 9), NORMAL(3, 18), LOUD(4, 36), SHOUT(5,
            60), LOUD_SHOUT(6, 80);

    private static final Distance[] VALUES = new Distance[7];

    static {
        for (Distance value : Distance.values()) {
            VALUES[value.index] = value;
        }
    }

    private final int index;
    private final int distance;

    Distance(int index, int distance) {
        this.index = index;
        this.distance = distance;
    }

    public static Distance get(int index) {
        return VALUES[index];
    }

    public Distance shift(int shift) {
        int index = Math.max(Math.min(this.index + shift, VALUES.length - 1), 0);
        return Distance.VALUES[index];
    }

    public int getIndex() {
        return index;
    }

    public int getDistance() {
        return distance;
    }
}
