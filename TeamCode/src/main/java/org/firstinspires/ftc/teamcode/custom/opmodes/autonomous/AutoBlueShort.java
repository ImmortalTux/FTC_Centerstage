package org.firstinspires.ftc.teamcode.custom.opmodes.autonomous;

import android.os.Environment;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.custom.Clock;
import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.custom.subsystems.Intake;
import org.firstinspires.ftc.teamcode.custom.subsystems.Lift;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "OpMode - Autonomous - Blue Short")
public class AutoBlueShort extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;

    private final int LIFT_MOTOR_RPM = 312;
    private final double LIFT_ENC_RESOLUTION = 537.7;
    private static final int ERROR_RANGE = 10;
    private static final int TICKS_PER_INCH = 1174; // TODO: Update this!

    private int currentStage;

    // 0 is unchanged (no side identified)
    // 1 is Left
    // 2 is Center
    // 3 is Right
    private int teamPropSide;

    // Stuff required for TensorFlow object detection.
    private TfodProcessor tfodProcessor = null;
    private VisionPortal visionPortal = null;
//    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/22347-teampiece.tflite";
    private static final String TFOD_MODEL_FILE = Environment.getExternalStorageDirectory().getPath() + "/FIRST/tflitemodels/22347-teampiece.tflite";
    private static final String[] MODEL_LABELS = {
            "Piece"
    };
    List<Recognition> currentRecognitions = null;

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

        currentStage = 0;

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

        lift.setLiftPosition((int)((((LIFT_MOTOR_RPM * LIFT_ENC_RESOLUTION) / 360) / 360) * 28) * 45);

        // Recognize where the team prop is placed.
        if (currentRecognitions.isEmpty()) {
            teamPropSide = 3;
        } else {
            for (Recognition recognition : currentRecognitions) {
                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                if (x > 0 && x < 213.3) {
                    teamPropSide = 1;                   // Team prop is on the left
                } else if (x > 213 && x < 426.3) {
                    teamPropSide = 2;                   // Team prop is in the center
                } else if (x > 426.3 && x < 640) {
                    teamPropSide = 3;                   // Team prop is on the right
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

        currentStage = 1;
    }

    /**
     * @brief       Main robot loop. All driving code goes here.
     */
    @Override
    public void loop() {
        Clock.updateDeltaTime();
        driveBase.odometry.update(telemetry);

        if (currentStage == 1) {
            lift.setLiftPosition(0);
            if (lift.getLiftMotorTicks() >= -ERROR_RANGE &&
                lift.getLiftMotorTicks() <= ERROR_RANGE) {
                // Lift position is 0 so 0 - ERROR_RANGE will just equal -ERROR_RANGE.
                // and the same goes for 0 + ERROR_RANGE, it will just be (+)ERROR_RANGE.
                currentStage++;
            }

        } else if (currentStage == 2) {
            if (!(driveBase.odometry.getLeftEncoderTicksRaw() >= (24 * TICKS_PER_INCH) - ERROR_RANGE &&
                driveBase.odometry.getLeftEncoderTicksRaw() <= (24 * TICKS_PER_INCH) + ERROR_RANGE)) {
                driveBase.moveSpeed(1, 0, 0);
            } else {
                driveBase.moveSpeed(0, 0, 0);
            }

        } else if (currentStage == 3) {
            switch (teamPropSide) {
                /* TODO: Possible replace driveBase.odometry.getPosition().heading with
                         anything else in case the odometry class isn't working. */
                case 1:
                    /* TODO: This probably needs to be changed. I need to figure out how
                     *   the odometry class in our drive base works. */
                    if (driveBase.odometry.getPosition().heading > 270 - (ERROR_RANGE / 2.0) &&
                        driveBase.odometry.getPosition().heading < 270 + (ERROR_RANGE / 2.0)) {
                        driveBase.moveSpeed(0, 0, 0);

                        currentStage++;

                    } else if (driveBase.odometry.getPosition().heading < 270) {
                        driveBase.moveSpeed(0, 0, -1);

                    } else if ( driveBase.odometry.getPosition().heading > 270) {
                        driveBase.moveSpeed(0, 0, 1);

                    }

                case 2:
                    // TODO: Nothing may need to be done here as the robot will already be facing the forward tape.

                case 3:
                    /* TODO: This probably needs to be changed. I need to figure out how
                     *   the odometry class in our drive base works. */
                    if (driveBase.odometry.getPosition().heading > 90 - (ERROR_RANGE / 2.0) &&
                            driveBase.odometry.getPosition().heading < 90 + (ERROR_RANGE / 2.0)) {
                        driveBase.moveSpeed(0, 0, 0);

                        currentStage++;

                    } else if (driveBase.odometry.getPosition().heading < 90) {
                        driveBase.moveSpeed(0, 0, -1);

                    } else if ( driveBase.odometry.getPosition().heading > 90) {
                        driveBase.moveSpeed(0, 0, 1);

                    }

                default:
            }

        } else if (currentStage == 4) {
            // TODO: Add some kind of logic to align the claw with the tape depending on each side.
            // TODO: Make sure the yellow pixel stays inside the claw while the purple claw is dropped onto the tape.
            intake.closeClaws(true, false);
            lift.setLiftPosition((int)((((LIFT_MOTOR_RPM * LIFT_ENC_RESOLUTION) / 360) / 360) * 28) * 10);

        } else if (currentStage == 5) {
            // TODO: Add code to drive towards the backstage area while also facing the backdrop.
            // AVOID THE PIXEL IF ON THE RIGHT SIDE!

        } else if (currentStage == 6) {
            /* TODO: Add code to either strafe across the backdrop or stay far back enough that
                     the camera can see all three AprilTags on the backdrop. */

        } else if (currentStage == 7) {
            // TODO: Raise lift high enough to place the remaining pixel on the backdrop.
            lift.setLiftPosition(Lift.LiftPosition.POSITION_LEVEL_2);
            lift.setArmPosition(Lift.ArmPosition.POSITION_LEVEL_2);

        } else if (currentStage == 8) {
            /* TODO: Drive forward until the claw is pressed against the backdrop.
                     Double check using the motor velocity. */

        } else if (currentStage == 9) {
            intake.closeClaws(false, false);

        } else if (currentStage == 10) {
            /* TODO: Drive back a small amount that frees the pixel and lets it fall while also
                     staying inside the parking zone for both sets of points. */

        } else {
            // There should NOT be any other states right now.
        }

        /// SECTION: Telemetry updating
        telemetry.addData("Current Stage:", currentStage);

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
