package org.firstinspires.ftc.teamcode.custom.subsystems;

import com.qualcomm.robotcore.hardware.*;

public class Lift extends Subsystem {
    private int liftTargetPosition = 0;

    private int LIFT_TARGET_MAX_DISTANCE = 50;

    /** @brief      Lift encoder positions, positions are in order of shortest arm position to
     *               longest. */

    static public class LiftPosition {
      static final public int ZERO = 0;

      static final public int POSITION_INTAKE = ZERO;
      static final public int POSITION_LEVEL_1 = 1800;
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

    /**
     * @brief       Initializes all resources required by lift
     *
     * @param       map: Hardware map. This should be passed by ;an OpMode object.
     */
    public Lift(HardwareMap map) {
        registerSubsystem(singleton, this);

        liftMotor = map.dcMotor.get("Lift-LiftMotor");
        armMotor = map.dcMotor.get("Lift-ArmMotor");

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

    public void update() {
        int tp = liftTargetPosition;
        int mp = liftMotor.getCurrentPosition();
        int absTp = Math.abs(tp);
        int absMp = Math.abs(mp);
        int thresh = 30;
        int k = absTp - absMp;
        double normalizedK = Math.min(k / 50, 1);

//        if ((tp + thresh) > mp && (tp - thresh) < mp){
//            liftMotor.setPower(0);
//        }
        if (tp < mp){
            liftMotor.setPower(-1 + normalizedK);
        } else if (tp > mp) {
            liftMotor.setPower(1 - normalizedK);
        }
    }
        /*
        if (liftTargetPosition < liftMotor.getCurrentPosition()) {

            liftMotor.setPower((Math.abs(liftTargetPosition) - Math.abs(liftMotor.getCurrentPosition()) > LIFT_TARGET_MAX_DISTANCE) ? -1.0 : 0.0);
            // return;
        } else if (liftTargetPosition >= liftMotor.getCurrentPosition()) {
            liftMotor.setPower((Math.abs(liftTargetPosition) - Math.abs(liftMotor.getCurrentPosition()) > LIFT_TARGET_MAX_DISTANCE) ? 1.0 : 0.0);

        } else {}
         */
//    }

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

    /**
     *
     */
    public int getLiftTargetPosition() {
        return liftTargetPosition;
    }
}
