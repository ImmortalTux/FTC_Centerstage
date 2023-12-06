package org.firstinspires.ftc.teamcode.custom;

import org.firstinspires.ftc.teamcode.custom.Clock;

/**
 * @brief Software implementation of PID controller.
 */
public class PIDController {
    /**
     * @brief        Distance between current position and target position.
     */
    private double currentError = 0.0;

    /**
     * @brief       errors accumulated since the last 20 iterations.
     */
    private double latestErrors[] = new double[20];

    /**
     * @brief       next index to overwrite from `latestErrors`
     */
    private int nextErrorIndex = 0;

    /**
     * @brief       Target position/value we want to reach.
     */
    private double target = 0.0;

    /**
     * @brief       Last output reported by PID controller.
     */
    private double pidOutput = 0.0;

    /**
     * @brief       Multipliers and Offset used to fine tune PID controller.
     */
    private final double proportionalGain, integralGain, derivativeGain, baseOffset;

    /**
     * @brief       Generates PID Controller and sets up a few constants.
     *
     * @param       proportionalGain: Constant modifying how much controller responds to current
     *               error.
     * @param       integralGain: Constant modifying how much controller responds to accumulated
     *               error.
     * @param       derivativeGain: Constant modifying how much controller reacts to the rate of
     *               change in error.
     * @param       baseOffset: Offset value always applied to final PID output. NOT A MULTIPLIER!
     */
    public PIDController(double proportionalGain, double integralGain, double derivativeGain,
                         double baseOffset) {
        this.proportionalGain = proportionalGain;
        this.integralGain = integralGain;
        this.derivativeGain = derivativeGain;
        this.baseOffset = baseOffset;
    }

    /**
     * @brief       Sets a new target for PID controller to reach.
     * @param       newTarget: New target position or value to reach.
     */
    public void setTarget(double newTarget) {
        target = newTarget;

        nextErrorIndex = 0;
        for (int i = 0; i < latestErrors.length; i++) {
            latestErrors[i] = 0.0;
        }
    }

    /**
     * @brief       Updates the PID Controller.
     * @note        this method should only be run once every iteration for each individual PID
     *              controller.
     * @note        This method also relies on Clock.getDeltaTime() to work properly, You should be
     *              updating delta time every frame too.
     *
     * @param       currentPosition: Current position of whatever PID Controller is attempting to
     *               control.
     */
    public void update(double currentPosition) {
        double previousError = currentError;
        currentError = target - currentPosition;

        latestErrors[nextErrorIndex] = currentError;
        nextErrorIndex = (nextErrorIndex + 1) % latestErrors.length;

        double totalError = 0.0;
        for (int i = 0; i < latestErrors.length; i++) {
            totalError += latestErrors[i];
        }

        double proportional = currentError * proportionalGain;
        double integral = totalError / latestErrors.length * integralGain;
        double derivative = (previousError - currentError) / Clock.getDeltaTime() * derivativeGain;
        pidOutput = proportional + integral + derivative + baseOffset;
    }

    /**
     * @brief       Returns PID output since last update.
     * @return      PID output.
     */
    public double getOutput() {
        return pidOutput;
    }
}
