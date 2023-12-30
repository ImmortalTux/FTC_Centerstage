package org.firstinspires.ftc.teamcode.custom.opmodes;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
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
@Config
public class AutoBlueShort extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;

    private final int LIFT_MOTOR_RPM = 312;
    private final double LIFT_ENC_RESOLUTION = 537.7;
    private final int ERROR_RANGE = 10;

    private int currentStage;
    private int errorRange;

    // 0 is unchanged (no side identified)
    // 1 is Left
    // 2 is Center
    // 3 is Right
    private int teamPropSide;

    // Stuff required for TensorFlow object detection.
    private TfodProcessor tfodProcessor = null;
    private VisionPortal visionPortal = null;
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/22347-teampiece.tflite";
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
        driveBase.motorSpeedMultiplier = 0.3;
        driveBase.odometry.resetEncoders();

        lift = new Lift(hardwareMap);

        intake = new Intake(hardwareMap);
        intake.closeClaws(true, true);

        currentStage = 0;
        errorRange = 30;

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
    }

    /**
     * @brief       Code ran repeatedly once robot is initialized. This will automatically stop
     *              execution once user hits PLAY button.
     */
    @Override
    public void init_loop() {
        currentRecognitions = tfodProcessor.getRecognitions();

        lift.setLiftPosition(
                (int)((((LIFT_MOTOR_RPM * LIFT_ENC_RESOLUTION) / 360) / 360) * 28) * 45
        );

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
                } else {
                    // TODO: Add error handling
                }
            }
        }

        telemetry.update();
    }

    /**
     * @brief       Ran once driver hits PLAY button.
     */
    @Override
    public void start() {
        Clock.init();

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

        } else if (currentStage == 2) {

        } else if (currentStage == 3) {

        } else if (currentStage == 4) {

        } else if (currentStage == 5) {

        } else if (currentStage == 6) {

        } else if (currentStage == 7) {

        } else if (currentStage == 8) {

        } else if (currentStage == 9) {

        } else if (currentStage == 10) {

        } else if (currentStage == 11) {

        } else if (currentStage == 12) {

        } else if (currentStage == 13) {

        } else {

        }

        /// All telemetry updating
        telemetry.addData("Current Piece Side", teamPropSide);
        telemetry.addData("Current Stage", currentStage);
        telemetry.addData("Current Lift Position", lift.getLiftMotorTicks());
        telemetry.addData("IDs", currentRecognitions);

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
