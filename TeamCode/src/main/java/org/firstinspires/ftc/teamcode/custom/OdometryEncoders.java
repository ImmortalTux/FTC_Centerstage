package org.firstinspires.ftc.teamcode.custom;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.custom.*;
import com.qualcomm.robotcore.hardware.*;

/**
 * @brief           Manages recordings from Odometry dead wheels.
 * @note            DO NOT implement this into an OpMode yourself. DriveBase subsystem will do this
 *                   for you.
 */
public class OdometryEncoders {
    /**
     * @brief       Estimated current position of robot.
     */
    private RobotPosition position = new RobotPosition(0.0, 0.0, 0.0);

    /* For more information about these variables, refer to:
        org.firstinspires.ftc.teamcode.custom.OdometryFlags */
    private final DcMotor leftEncoder, rightEncoder, backEncoder;
    private final double wheelbaseDistance;
    private final int ticksPerRevolution;

    /**
     * @brief       Circumference calculated from `OdometryFlags.odometryWheelRadius`
     */
    private final double wheelCircumference;

    /**
     * @brief       Encoder positions read last time odometry encoders were updated.
     */
    private int lastLeftEncoderPos = 0, lastRightEncoderPos = 0, lastBackEncoderPos = 0;

    /**
     * @brief       Initializes odometry encoders for calculating robot position from specified
     *               flags.
     *
     * @param       flags: Flags required to initialize odometry encoders.
     */
    public OdometryEncoders(OdometryFlags flags) {
        leftEncoder = flags.leftOdometryEncoder;
        rightEncoder = flags.rightOdometryEncoder;
        backEncoder = flags.backOdometryEncoder;
        wheelbaseDistance = flags.wheelbaseDistance;
        ticksPerRevolution = flags.ticksPerRevolution;
        wheelCircumference = 2 * Math.PI * flags.odometryWheelRadius;

        resetEncoders();
    }

    /**
     * @brief   Calculates position from Odometry encoder data. This should be called every frame.
     */
    public void update(Telemetry telem) {
        double leftVelocity =
                (double) (leftEncoder.getCurrentPosition() - lastLeftEncoderPos) / ticksPerRevolution * wheelCircumference;
        double rightVelocity =
                (double) (rightEncoder.getCurrentPosition() - lastRightEncoderPos) / ticksPerRevolution * wheelCircumference;
        double backVelocity =
                (double) (backEncoder.getCurrentPosition() - lastBackEncoderPos) / ticksPerRevolution * wheelCircumference;

        telem.addData("left Encoder Velocity", leftVelocity);
        telem.addData("right Encoder Velocity", rightVelocity);
        telem.addData("back Encoder Velocity", backVelocity);

        double headingVelocity = (leftVelocity - rightVelocity) / wheelbaseDistance;
        double untransformedVelocityX = (leftVelocity + rightVelocity) / 2.0;
        /* backVelocity - forward offset may be needed! */
        double untransformedVelocityY = backVelocity;

        double headingSin = Math.sin(position.heading);
        double headingCos = Math.cos(position.heading);

        position.x += untransformedVelocityX * headingCos - untransformedVelocityY * headingSin;
        position.y += untransformedVelocityX * headingSin + untransformedVelocityY * headingCos;
        position.heading += headingVelocity;

        /* Preparing for next update. */
        lastLeftEncoderPos = leftEncoder.getCurrentPosition();
        lastRightEncoderPos = rightEncoder.getCurrentPosition();
        lastBackEncoderPos = backEncoder.getCurrentPosition();
    }

    /**
     * @brief       Resets all encoder values to zero.
     */
    public void resetEncoders() {
        DcMotor.RunMode leftEncoderLastMode = leftEncoder.getMode();
        DcMotor.RunMode rightEncoderLastMode = rightEncoder.getMode();
        DcMotor.RunMode backEncoderLastMode = backEncoder.getMode();

        leftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftEncoder.setMode(leftEncoderLastMode);
        rightEncoder.setMode(rightEncoderLastMode);
        backEncoder.setMode(backEncoderLastMode);

        position.x = 0.0;
        position.y = 0.0;
        position.heading = 0.0;
    }

    /**
     * @brief       Returns the estimated position of the robot, in encoder ticks.
     * @note        X and Y values correlate to the robot position, relative to initialization.
     *               Vector Z value correlates to robot heading.
     * @return      Estimated robot position.
     */
    public RobotPosition getPosition() {
        return position;
    }

    /**
     * @return      Returns encoder ticks directly returned from motor encoder.
     */
    public int getLeftEncoderTicksRaw() {
        return leftEncoder.getCurrentPosition();
    }

    /**
     * @return      Returns encoder ticks directly returned from motor encoder.
     */
    public int getRightEncoderTicksRaw() {
        return rightEncoder.getCurrentPosition();
    }

    /**
     * @return      Returns encoder ticks directly returned from motor encoder.
     */
    public int getBackEncoderTicksRaw() {
        return backEncoder.getCurrentPosition();
    }
}
