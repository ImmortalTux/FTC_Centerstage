package org.firstinspires.ftc.teamcode.custom.math;

public class Clamp {
    public static int clamp(int val, int min, int max) {
        return Math.max(Math.min(val, max), min);
    }
}
