package org.firstinspires.ftc.teamcode.custom.subsystems;

import com.qualcomm.robotcore.hardware.*;

public class Intake extends Subsystem {
    /**
     * @brief       used to ensure one Intake object is being used at a time.
     */
    private Subsystem.Singleton singleton = new Subsystem.Singleton();

    /**
     * @brief       Servos controlling right and left claws respectively.
     */
    private final Servo rightClaw, leftClaw;

    /**
     * @brief       Servo used to rotate entire Intake up and down.
     */
    private final Servo wristVertical, wristHorizontal;

    /**
     * @brief       Color sensors used to confirm that a pixel is in a given chamber.
     */
    private final ColorSensor leftPixelConfirmation, rightPixelConfirmation;

    /**
     * @brief       Initializes all resources required by Intake.
     *
     * @param       map: Hardware map. This should be passed by an OpMode object.
     */
    public Intake(HardwareMap map) {
        registerSubsystem(singleton, this);

        rightClaw = map.servo.get("Intake-RightClaw");
        leftClaw = map.servo.get("Intake-LeftClaw");
        leftClaw.setDirection(Servo.Direction.REVERSE);

        wristVertical = map.servo.get("Intake-WristVertical");
        wristHorizontal = map.servo.get("Intake-WristHorizontal");

        leftPixelConfirmation = map.colorSensor.get("Intake-LeftPixelConfirmation");
        rightPixelConfirmation = map.colorSensor.get("Intake-RightPixelConfirmation");

        /* Vertical servo is level with ground at position 1.0 */
        setWristPosition(0.0, 1.0);
    }

    /**
     * @brief       Call this function whenever you are done using system object.
     */
    public void cleanup() {
        unregisterSubsystem(singleton, this);
    }

    /**
     * @brief       Closes Left and right intake claws.
     *
     * @param       leftToggle: toggles whether or not to close left claw.
     * @param       rightToggle: toggles whether or not to close right claw.
     */
    public void closeClaws(boolean leftToggle, boolean rightToggle) {
        final float CLAW_POSITION_CLOSE = 0.85f;
        final float CLAW_POSITION_OPEN = 0.35f;;

        rightClaw.setPosition((rightToggle) ? CLAW_POSITION_CLOSE : CLAW_POSITION_OPEN);
        leftClaw.setPosition((leftToggle) ? CLAW_POSITION_CLOSE : CLAW_POSITION_OPEN);
    }

    /**
     * TODO: Documentation
     */
    public void setWristPosition(double horizontal, double vertical) {
        wristHorizontal.setPosition(horizontal);
        wristVertical.setPosition(vertical);
    }

    /**
     * @brief       Returns the current horizontal wrist servo encoder value as a double.
     */
    public double getWristHorizontalPosition() {
        return wristHorizontal.getPosition();
    }

    /**
     * @brief       Returns the current vertical wrist servo encoder value as a double.
     */
    public double getWristVerticalPosition() {
        return wristVertical.getPosition();
    }

    /**
     * @brief       Returns the left claw position as a double.
     */
    public double getLeftClawPosition() { return leftClaw.getPosition(); }

    /**
     * @brief       Returns the right claw position as a double.
     */
    public double getRightClawPosition() { return rightClaw.getPosition(); }

    public void setLeftClaw(float pos) {
        leftClaw.setPosition(pos);
    }

    public void setRightClaw(float pos) {
        rightClaw.setPosition(pos);
    }
}
