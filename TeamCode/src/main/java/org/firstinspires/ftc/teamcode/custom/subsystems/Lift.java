package org.firstinspires.ftc.teamcode.custom.subsystems;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.custom.math.MUtils;

public class Lift extends Subsystem {
    private int liftTargetPosition = 0;

    private int LIFT_TARGET_MAX_DISTANCE = 50;

    /** @brief      Lift encoder positions, positions are in order of shortest arm position to
     *               longest. */

    static public class LiftPosition {
      static final public int ZERO = 0;

      static final public int POSITION_INTAKE = ZERO;
      static final public int POSITION_LEVEL_1 = 2000;
      static final public int POSITION_LEVEL_2 = 1800;

      static final public int POSITION_LEVEL_3 = 1800;

      static final public int POSITION_VERTICAL = 3820;
    };

    /** @brief      Arm encoder positions, positions are in order of shortest arm position to
     *               longest. */
    static public class ArmPosition {
        static final public int ZERO = 0;
        static final public int POSITION_INTAKE = 500;
        static final public int POSITION_LEVEL_1 = 700;

        static final public int POSITION_LEVEL_2 = 900;

        static final public int POSITION_LEVEL_3 = 1100;
    }

    /**
     * @brief       Used to ensure one VisionSensor object is being used at a time.
     */
    static private Subsystem.Singleton singleton = new Subsystem.Singleton();

    /**
     * @brief       Motor responsible for controlling the robot lift.
     */
    private final DcMotor liftMotor, armMotor;

    /*
     * @brief       Used to confirm and help lift ensure it's touching the ground properly.
     * @note        this touch sensor only returns values 1 and 0.
     */
    private final TouchSensor groundConfirmation;

    /**
     * @brief       Initializes all resources required by lift
     *
     * @param       map: Hardware map. This should be passed by ;an OpMode object.
     */
    public Lift(HardwareMap map) {
        registerSubsystem(singleton, this);

        liftMotor = map.dcMotor.get("Lift-LiftMotor");
        armMotor = map.dcMotor.get("Lift-ArmMotor");
        groundConfirmation = map.touchSensor.get("Lift-GroundConfirmation");

        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotor.setPower(1.0);

        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setPower(1.0);
    }

    /**
     * @brief       Call this function whenever you are done using system object.
     */
    public void cleanup() {
        unregisterSubsystem(singleton, this);
    }

    /**
     * @brief       Sets target position for lift to move to vertically.
     *
     * @param       position: Target encoder tick position.
     */
    public void setLiftPosition(int position) {
//        liftTargetPosition = position;
        liftMotor.setTargetPosition(position);
    }

    /**
     * @brief       Sets target position for lift to move out horizontally.
     * @param       position: Target encoder tick position.
     */
    public void setArmPosition(int position) {
        armMotor.setTargetPosition(position);
    }

    /**
     * @return      Current lift motor position.
     */
    public int getLiftMotorTicks() {
        return liftMotor.getCurrentPosition();
    }

    /**
     * @return      Current arm motor position.
     */
    public int getArmMotorTicks() {
        return armMotor.getCurrentPosition();
    }

    public DcMotor getLiftMotor() { return liftMotor; }

    /**
     *
     */
    public int getLiftTargetPosition() {
        return liftMotor.getTargetPosition();
    }

    public void update() {
        if (groundConfirmation.isPressed()) {
            setLiftPosition(0);
            liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            return;
        }

        int liftTargetPos = getLiftTargetPosition();
        if (!groundConfirmation.isPressed() && liftTargetPos == LiftPosition.ZERO &&
                MUtils.withinRange(getLiftMotorTicks(), liftTargetPos, 2)) {
            setLiftPosition(liftTargetPos - 1);
        }
    }
}
