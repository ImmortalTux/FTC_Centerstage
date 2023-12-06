package org.firstinspires.ftc.teamcode.custom.subsystems;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.custom.OdometryEncoders;
import org.firstinspires.ftc.teamcode.custom.OdometryFlags;

/**
 * @brief           Variables and procedures relating to drive base subsystem.
 */
public class DriveBase extends Subsystem {
    /**
     * @brief       used to ensure one DriveBase object is being used at a time.
     */
    static private Subsystem.Singleton singleton = new Subsystem.Singleton();

    /**
     * @brief Odometry encoders. Handled by another class.
     */
    public final OdometryEncoders odometry;

    /**
     * @brief       motors Allocated towards controlling the robot's Mecanum Wheels. positions are
     *               relative to the back of the robot.
     */
    public final DcMotor frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel;

    /**
     * @brief       Servos used for lifting and dropping servos from the ground.
     */
    private final Servo rightOdometryLift, leftOdometryLift, backOdometryLift;

    /**
     * @brief       Multiplies speed of all motors in drive base. Could be used to tune how
     *              responsive a joystick might be...
     */
    public double motorSpeedMultiplier = 1.0;

    /**
     * @brief       Initializes all dependencies required by drive base.
     *
     * @param       map: Hardware map. This should be passed by an OpMode object.
     */
    public DriveBase(HardwareMap map) {
        registerSubsystem(singleton, this);

        frontLeftWheel = map.dcMotor.get("DriveBase-FrontLeftWheel");
        frontRightWheel = map.dcMotor.get("DriveBase-FrontRightWheel");
        backLeftWheel = map.dcMotor.get("DriveBase-BackLeftWheel");
        backRightWheel = map.dcMotor.get("DriveBase-BackRightWheel");

        rightOdometryLift = map.servo.get("DriveBase-RightOdometryLift");
        leftOdometryLift = map.servo.get("DriveBase-LeftOdometryLift");
        backOdometryLift = map.servo.get("DriveBase-BackOdometryLift");

        /*
            frontLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        */

        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        backRightWheel.setDirection(DcMotor.Direction.REVERSE);

        OdometryFlags flags = new OdometryFlags();
        flags.leftOdometryEncoder = frontLeftWheel;
        flags.rightOdometryEncoder = frontRightWheel;
        flags.backOdometryEncoder = backLeftWheel;
        flags.wheelbaseDistance = 10.25;
        flags.ticksPerRevolution = 8192;
        flags.odometryWheelRadius = 1.0;

        odometry = new OdometryEncoders(flags);
        dropOdometry(false);
    }

    /**
     * @brief       Call this function whenever you are done using system object.
     */
    public void cleanup() {
        dropOdometry(false);
        odometry.resetEncoders();
        motorSpeedMultiplier = 0.0;

        unregisterSubsystem(singleton, this);
    }

    /**
     * @brief       Toggles whether or not odometry wheels should be lifted off/dropped onto the
     *               ground.
     *
     * @param       toggle: 1 to drop odometry wheels, 0 to lift odometry wheels.
     */
    public void dropOdometry(boolean toggle) {
        final double DROP_POSITION = 0.5;
        final double LIFT_POSITION = 0;
        double target = toggle ? DROP_POSITION : LIFT_POSITION;

        rightOdometryLift.setPosition(target);
        /* Left servo orientation opposite of others. */
        leftOdometryLift.setPosition(1.0 - target);
        backOdometryLift.setPosition(target);
    }

    /**
     * @brief       Sets drive base motors to move at a certain direction and speed.
     * @note        All parameter values will be clamped between 1 and -1. Both of these numbers
     *               represent maximum speed/power in either direction.
     *
     * @param       forward: Movement on the robots y axis (relative to robots current position.)
     *               Positive values will move the robot forward, negative values will move it
     *               backwards.
     * @param       strafe: Movement on the robots x axis (relative to robots current position and
     *               back face.) Positive numbers will move the robot to its right, negative values
     *               will move it to its left.
     * @param       rotation: Speed at which to pivot the robot. Negative values rotate
     *               counterclockwise, positive values rotate clockwise.
     */
    public void moveSpeed(double forward, double strafe, double rotation) {
        /* Deprecated
            frontLeftWheel.setPower((forward + strafe + rotation) * motorSpeedMultiplier);
            frontRightWheel.setPower((forward - strafe - rotation) * motorSpeedMultiplier);
            backLeftWheel.setPower((forward - strafe + rotation) * motorSpeedMultiplier);
            backRightWheel.setPower((forward + strafe - rotation) * motorSpeedMultiplier);
        */

        double velocityAngle = Math.atan2(forward, strafe);
        double speed = Math.hypot(strafe, forward);

        /* 0.785398163397 = pi / 4, or a 45 degree angle in radians */
        double sinTemp = Math.sin(velocityAngle - 0.785398163397);
        double cosTemp = Math.cos(velocityAngle - 0.785398163397);

        /* Ensures that at least one set of motors is running at the maximum they are allowed.
        Remember that the Drive Base does not travel in all directions at the same speed. */
        double maxSinCos = Math.max(Math.abs(sinTemp), Math.abs(cosTemp));
        sinTemp /= maxSinCos;
        cosTemp /= maxSinCos;

        frontLeftWheel.setPower((speed * cosTemp + rotation) * motorSpeedMultiplier);
        frontRightWheel.setPower((speed * sinTemp - rotation) * motorSpeedMultiplier);
        backLeftWheel.setPower((speed * sinTemp + rotation) * motorSpeedMultiplier);
        backRightWheel.setPower((speed * cosTemp - rotation) * motorSpeedMultiplier);
    }
}
