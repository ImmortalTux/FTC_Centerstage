package org.firstinspires.ftc.teamcode.custom.opmodes.autonomous.enc;

import android.speech.tts.TextToSpeech;
import android.util.Size;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.custom.Clock;
import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.custom.subsystems.Intake;
import org.firstinspires.ftc.teamcode.custom.subsystems.Lift;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Autonomous(name = "Autonomous - Red Short - Encoders")
public class AutoRedShortEnc extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;

    private IMU imu = null;
    private double heading;

    private final int LIFT_MOTOR_RPM = 312;
    private final double LIFT_ENC_RESOLUTION = 537.7;
    private static final int ERROR_RANGE = 300;
    private static final int TICKS_PER_INCH = 1711;
    private static final int TICKS_PER_RIGHT_TURN = 11500;

    private int currentState;

    // 0 is unchanged (no side identified)
    // 1 is Left
    // 2 is Center
    // 3 is Right
    private int teamPropSide;

    private boolean resetEncoders;

    // Stuff required for TensorFlow object detection.
    private TfodProcessor tfodProcessor = null;
    private VisionPortal visionPortal = null;
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/22347-teampiece.tflite";
    // private static final String TFOD_MODEL_FILE = Environment.getExternalStorageDirectory().getPath() + "/FIRST/tflitemodels/22347-teampiece.tflite";
    private static final String[] MODEL_LABELS = {
            "Piece"
    };
    List<Recognition> currentRecognitions = null;

    private AprilTagProcessor aprilTagProcessor;

    /**
     * @brief       Code ran to initialize robot.
     */
    @Override
    public void init() {
        driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);
        driveBase.motorSpeedMultiplier = 0.33;
        driveBase.odometry.resetEncoders();

        lift = new Lift(hardwareMap);

        intake = new Intake(hardwareMap);
        intake.closeClaws(true, true);
        intake.leftClaw.setDirection(Servo.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                        )
                )
        );
        imu.resetYaw();
        heading = 0.0;

        currentState = 0;

        resetEncoders = true;

        /// SECTION: Initialization of the TensorFlow Processor
        tfodProcessor = new TfodProcessor.Builder()
                .setModelFileName(TFOD_MODEL_FILE)
                .setModelLabels(MODEL_LABELS)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "VisionSensor-Webcam"))
                .setCameraResolution(new Size(640, 480))
                .enableLiveView(false)
                .addProcessor(tfodProcessor)
                .build();

        visionPortal.setProcessorEnabled(tfodProcessor, true);
        tfodProcessor.setMinResultConfidence(0.75f);

        // aprilTagProcessor =
        //     new AprilTagProcessorBuilder
        //         .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
        //         .build();

        telemetry.addLine("REMEMBER!");
        telemetry.addLine("Wait at least 5-10 seconds to MAKE sure that TensorFlow can find the team prop.");
        telemetry.addLine("If you immediately start the program, there is a chance that it will not find the team prop.");
        telemetry.update();
    }

    /**
     * @brief       Code ran repeatedly once robot is initialized. This will automatically stop
     *              execution once user hits PLAY button.
     */
    @Override
    public void init_loop() {
        currentRecognitions = tfodProcessor.getRecognitions();

        // Recognize where the team prop is placed.
        if (currentRecognitions.isEmpty()) {
            teamPropSide = 3;
        } else {
            for (Recognition recognition : currentRecognitions) {
                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                if (x > 0 && x < 320) {
                    teamPropSide = 1;                   // Team prop is on the left
                } else {
                    teamPropSide = 2;                   // Team prop is in the center
                }
            }
        }

        telemetry.addData("Current Piece Side", teamPropSide);
        telemetry.addData("IDs", currentRecognitions);
        telemetry.update();
    }

    /**
     * @brief       Ran once driver hits PLAY button.
     */
    @Override
    public void start() {
        Clock.init();
        telemetry.clearAll();

        currentState = 1;
    }

    /**
     * @brief       Main robot loop. All driving code goes here.
     */
    @Override
    public void loop() {
        Clock.updateDeltaTime();
        driveBase.odometry.update(telemetry);

        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        if (currentState == 1) {
            lift.setLiftPosition(0);
            if (lift.getLiftMotorTicks() >= -ERROR_RANGE &&
                lift.getLiftMotorTicks() <= ERROR_RANGE) {
                // Lift position is 0 so 0 - ERROR_RANGE will just equal -ERROR_RANGE.
                // and the same goes for 0 + ERROR_RANGE, it will just be (+)ERROR_RANGE.
                resetEncoders = true;
                currentState++;
            }

        } else if (currentState == 2) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            if (Math.abs(driveBase.odometry.getLeftEncoderTicksRaw()) >= (24 * TICKS_PER_INCH) - ERROR_RANGE &&
                Math.abs(driveBase.odometry.getRightEncoderTicksRaw()) >= (24 * TICKS_PER_INCH) - ERROR_RANGE) {
                driveBase.moveSpeed(0, 0, 0);
                resetEncoders = true;
                currentState++;
            } else {
                driveBase.moveSpeed(-1, 0, 0);
            }
        } else if (currentState == 3) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            switch (teamPropSide) {
                case 1:
                    if (
                        // driveBase.odometry.getBackEncoderTicksRaw() >= -TICKS_PER_RIGHT_TURN - ERROR_RANGE &&
                        driveBase.odometry.getBackEncoderTicksRaw() <= -TICKS_PER_RIGHT_TURN /* + ERROR_RANGE */
                    ) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(0, 0, 1);
                    }
                    break;

                case 2:
                    resetEncoders = true;
                    currentState++;

                    break;

                case 3:
                    if (
                        // heading >= -90 - (ERROR_RANGE / 4.0) &&
                        // heading <= -90 + (ERROR_RANGE / 4.0)
                        driveBase.odometry.getBackEncoderTicksRaw() >= TICKS_PER_RIGHT_TURN /* - ERROR_RANGE && */
                        // driveBase.odometry.getBackEncoderTicksRaw() <= TICKS_PER_RIGHT_TURN + ERROR_RANGE
                    ) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(0, 0, -1);
                    }

                    break;
                default:
                    break;
            }

        } else if (currentState == 4) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            switch (teamPropSide) {
                case 1:
                    lift.setArmPosition(300);

                    break;

                case 2:
                    lift.setArmPosition(400);

                    break;

                case 3:
                    lift.setArmPosition(250);

                    break;
                    
                default:
                    break;
            }

            resetEncoders = true;
            currentState++;
                
        } else if (currentState == 5) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            // Open the claws to release and slightly raise the lift.
            intake.closeClaws(true, false);

            lift.setLiftPosition(
                    (int) ((((312 * 537.7) / 360) / 360) * 28) * 45
            );

            if (lift.getLiftMotorTicks() >= lift.getLiftTargetPosition() - ERROR_RANGE &&
                lift.getLiftMotorTicks() <= lift.getLiftTargetPosition() + ERROR_RANGE) {
                resetEncoders = true;
                currentState++;
            }

        } else if (currentState == 6) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            // TODO: Add code to turn bot
            switch (teamPropSide) {
                case 1:
                    if (driveBase.odometry.getBackEncoderTicksRaw() <= -27550) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(0, 0, 1);
                    }

                    break;

                case 2:
                    if (driveBase.odometry.getBackEncoderTicksRaw() >= TICKS_PER_RIGHT_TURN)
                        /* (driveBase.odometry.getBackEncoderTicksRaw() * -1) <= TICKS_PER_RIGHT_TURN + ERROR_RANGE) */ {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(0, 0, -1);
                    }

                    break;

                case 3:
                    driveBase.backOdometryLift.setPosition(0.0);
                    resetEncoders = true;
                    currentState++;

                    break;

                default:
                    break;
            }

        } else if (currentState == 7) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            switch (teamPropSide) {
                case 1:
                    if (Math.abs(driveBase.odometry.getLeftEncoderTicksRaw()) >= (36 * TICKS_PER_INCH) - ERROR_RANGE) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(-1, 0, 0);
                    }
                    
                case 2:
                    if (Math.abs(driveBase.odometry.getLeftEncoderTicksRaw()) >= (36 * TICKS_PER_INCH) - ERROR_RANGE) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(-1, 0, 0);
                    }

                case 3:
                    if (Math.abs(driveBase.odometry.getLeftEncoderTicksRaw()) >= (38 * TICKS_PER_INCH) - ERROR_RANGE) {
                        driveBase.moveSpeed(0, 0, 0);
                        resetEncoders = true;
                        currentState++;

                        break;
                    } else {
                        driveBase.moveSpeed(-1, 0, 0);
                    }
                    
                default:
                    break;
            }

        } else if (currentState == 8) {
            lift.setLiftPosition(Lift.LiftPosition.POSITION_LEVEL_2);
            lift.setArmPosition(Lift.ArmPosition.POSITION_LEVEL_1);

            resetEncoders = true;
            currentState++;

        } else if (currentState == 9) {
            if (resetEncoders) { driveBase.odometry.resetEncoders(); resetEncoders = false; }

            intake.closeClaws(false, false);

            resetEncoders = true;
            currentState++;

        } else {
            // There should NOT be any other states right now.
        }

        /// SECTION: Telemetry updating
        telemetry.addData("Current Stage:", currentState);

        telemetry.addData("L Odo", driveBase.odometry.getLeftEncoderTicksRaw());
        telemetry.addData("R Odo", driveBase.odometry.getRightEncoderTicksRaw());
        telemetry.addData("B Odo", driveBase.odometry.getBackEncoderTicksRaw());
        telemetry.addData("Heading", heading);
        telemetry.addData("Real Heading", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

        telemetry.update();
    }

    /**
     * @brief       Ran once robot is told to stop. Cleanup code goes here.
     */
    @Override
    public void stop() {
        driveBase.cleanup();
        lift.cleanup();
        intake.cleanup();

        visionPortal.close();
    }
}
