package org.firstinspires.ftc.teamcode.custom.math;

public class MUtils {
    public static int clamp(int val, int min, int max) {
        return Math.max(Math.min(val, max), min);
    }

    public static boolean withinRange(int val1, int val2, int error) {
        return (Math.abs(val1 - val1) <= Math.abs(error));
    }
}
