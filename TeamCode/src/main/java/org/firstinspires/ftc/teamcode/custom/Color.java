package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class Color {
    public int r, g, b, a;

    public Color(int r, int g, int b, int a) {
        setRGBA(r, g, b, a);
    }

    public Color(ColorSensor color) {
        setColorSensor(color);
    }

    public void setRGBA(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setColorSensor(ColorSensor color) {
        r = color.red();
        g = color.green();
        b = color.blue();
        a = color.alpha();
    }

    /* Compares two color values within a given `tolerance.` returns true if these values are within
        specific tolerance of each other. */
    static public boolean compare(Color a, Color b, int tolerance) {
        tolerance = Math.abs(tolerance);

        int diffR = Math.abs(a.r - b.r);
        int diffG = Math.abs(a.g - b.g);
        int diffB = Math.abs(a.b - b.b);
        int diffA = Math.abs(a.a - b.a);

        return (diffR <= tolerance && diffG <= tolerance && diffB <= tolerance
                && diffA <= tolerance);
    }
}
