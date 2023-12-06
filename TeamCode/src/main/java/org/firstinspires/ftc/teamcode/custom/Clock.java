package org.firstinspires.ftc.teamcode.custom;

public class Clock {
    /**
     * @brief       `System.nanoTime()` last iteration.
     */
    static private long lastIterationTick = 0;

    /**
     * @brief       Time between each iteration of code.
     */
    static private double deltaTime = 0.0001;

    /**
     * @brief       target amount of nanoseconds between each iteration.
     */
    static private long targetIterationTime = 1;

    /**
     * @brief       Used to initialize internal static Clock systems. Recommended that you run this
     *              within your OpMode's start function.
     */
    static public void init() {
        lastIterationTick = System.nanoTime();
    }

    /**
     * @brief       Updates internal deltaTime variable for later use. Please call this once every
     *              iteration of loop().
     * @note        This will preoccupy whichever thread this is run in until the set target
     *              iterations per second is met.
     */
    static public void updateDeltaTime() {;
        long iterationTime = System.nanoTime() - lastIterationTick;

        while (iterationTime < targetIterationTime) {
            /* Can't call sleep() or robot kills itself */
            iterationTime = System.nanoTime() - lastIterationTick;
        }

        deltaTime = iterationTime / 1.0e9; /* 1.0e9 = One billion */
        lastIterationTick = System.nanoTime();
    }

    /**
     * @brief       Returns time between iterations in seconds.
     * @return      Delta time.
     */
    static public double getDeltaTime() { return deltaTime; }

    /**
     * @brief       Sets a target amount of iterations the robot should go through every second.
     * @param       ips: Target iterations per second.
     */
    static public void limitIterationsPerSecond(int ips) {
        if (ips < 1) {
            targetIterationTime = 1;
            return;
        }

        targetIterationTime = (long) 1.0e9 / ips;
    }
}
