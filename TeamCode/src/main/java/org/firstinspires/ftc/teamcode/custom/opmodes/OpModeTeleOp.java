package org.firstinspires.ftc.teamcode.custom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.*;
import org.firstinspires.ftc.teamcode.custom.math.MUtils;
import org.firstinspires.ftc.teamcode.custom.subsystems.*;
import org.firstinspires.ftc.teamcode.custom.subsystems.DroneLauncher;

@TeleOp(name = "OpMode - TeleOp")
public class OpModeTeleOp extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;
    private DroneLauncher droneLauncher = null;

    private int liftPositionInputBuffer = 0, armPositionInputBuffer = 0;
    private boolean leftClawInputBuffer = true, rightClawInputBuffer = true;
    private boolean lastLeftBumperValue = false, lastRightBumperValue = false;

    private double wristVerticalInputBuffer = 0.0, wristHorizontalInputBuffer = 0.0;

    private final int LIFT_MOVE_TPS = 1000, ARM_MOVE_TPS = 800;

    /**
     * @brief       Code ran to initialize robot.
     */
    @Override
    public void init() {
        driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);

        lift = new Lift(hardwareMap);
        intake = new Intake(hardwareMap);
        intake.closeClaws(leftClawInputBuffer, rightClawInputBuffer);

        droneLauncher = new DroneLauncher(hardwareMap);
    }

    /**
     * @brief       Code ran repeatedly once robot is initialized. This will automatically stop
     *              execution once user hits PLAY button.
     */
    @Override
    public void init_loop() {

    }

    /**
     * @brief       Ran once driver hits PLAY button.
     */
    @Override
    public void start() { Clock.init(); }

    /**
     * @brief       Main robot loop. All driving code goes here.
     */
    @Override
    public void loop() {
        Clock.updateDeltaTime();
        lift.update();

        /* Drive base */
        driveBase.motorSpeedMultiplier = 0.8 - Math.min(gamepad1.left_trigger, 0.5);
        driveBase.moveSpeed(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

        /* Lift and Arm */
        liftPositionInputBuffer += (int) (LIFT_MOVE_TPS * -gamepad2.left_stick_y * Clock.getDeltaTime());
        armPositionInputBuffer += (int) (ARM_MOVE_TPS * -gamepad2.right_stick_y * Clock.getDeltaTime());

        if (gamepad2.start) {
            liftPositionInputBuffer = Lift.LiftPosition.ZERO;
            armPositionInputBuffer = Lift.ArmPosition.ZERO;
        } else if (gamepad2.dpad_down) {
            liftPositionInputBuffer = Lift.LiftPosition.POSITION_INTAKE;
            armPositionInputBuffer = Lift.ArmPosition.POSITION_INTAKE;
        } else if (gamepad2.dpad_left && armPositionInputBuffer > Lift.ArmPosition.ZERO) {
            liftPositionInputBuffer = Lift.LiftPosition.POSITION_LEVEL_1;
            armPositionInputBuffer = Lift.ArmPosition.POSITION_LEVEL_1;
        } else if (gamepad2.dpad_right && armPositionInputBuffer > Lift.ArmPosition.ZERO) {
            liftPositionInputBuffer = Lift.LiftPosition.POSITION_LEVEL_2;
            armPositionInputBuffer = Lift.ArmPosition.POSITION_LEVEL_2;
        } else if (gamepad2.dpad_up && armPositionInputBuffer > Lift.ArmPosition.ZERO) {
            liftPositionInputBuffer = Lift.LiftPosition.POSITION_LEVEL_3;
            armPositionInputBuffer = Lift.ArmPosition.POSITION_LEVEL_3;
        }

        liftPositionInputBuffer = gamepad2.x ?
                MUtils.clamp(liftPositionInputBuffer, Lift.LiftPosition.ZERO - 20, Lift.LiftPosition.POSITION_VERTICAL)
                :
                MUtils.clamp(liftPositionInputBuffer, Lift.LiftPosition.ZERO, Lift.LiftPosition.POSITION_VERTICAL);
        armPositionInputBuffer = MUtils.clamp(armPositionInputBuffer, Lift.ArmPosition.ZERO - 20, Lift.ArmPosition.POSITION_LEVEL_3);
        lift.setLiftPosition(liftPositionInputBuffer);
        lift.setArmPosition(armPositionInputBuffer);

        /* Intake */
        if (gamepad2.left_bumper && !lastLeftBumperValue) {
            rightClawInputBuffer = !rightClawInputBuffer;
        }

        if (gamepad2.right_bumper && !lastRightBumperValue) {
            leftClawInputBuffer = !leftClawInputBuffer;
        }

        if (gamepad2.start) {
            leftClawInputBuffer = true;
            rightClawInputBuffer = true;
        }

        if (gamepad2.left_trigger >= 1.0 && gamepad2.right_trigger >= 1.00) {
            droneLauncher.releaseDrone();
        }

        intake.closeClaws(leftClawInputBuffer, rightClawInputBuffer);
        intake.setWristPosition(0.0, 1.0);

        /* Misc */
        lastLeftBumperValue = gamepad2.left_bumper;
        lastRightBumperValue = gamepad2.right_bumper;

        /* TTS */
        if (gamepad1.a) {
            telemetry.update();
            telemetry.speak("Finger", null, null);
            telemetry.update();
        }
    }

    /**
     * @breif       Ran once robot is told to stop. Cleanup code goes here.
     */
    @Override
    public void stop() {
        driveBase.cleanup();
        lift.cleanup();
        intake.cleanup();
    }
}
