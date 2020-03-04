package ru.xunto.roleplaychat.api;

public class Position {
    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public double distance(double x1, double y1, double z1) {
        double x2 = this.getX() - x1;
        double y2 = this.getY() - y1;
        double z2 = this.getZ() - z1;
        return Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
    }

    public double distance(Position var1) {
        return this.distance(var1.getX(), var1.getY(), var1.getZ());
    }
}
