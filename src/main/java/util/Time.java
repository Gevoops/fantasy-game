package util;

public class Time {
    public static long timeStarted = System.nanoTime(); // big number
    public static long timePassed = 0;

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
