package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.subsystems.VisionSensor;

@TeleOp(name = "Test - Vision Sensor")
public class TestVisionSensor extends LinearOpMode {
    private VisionSensor visionSensor = null;

    public void runOpMode() {
        visionSensor = new VisionSensor(
                hardwareMap,
                "/sdcard/FIRST/tflitemodels/22347-teampiece.tflite"
        );

        waitForStart();
        while (opModeIsActive()) {

            idle();
        }

        visionSensor.cleanup();
    }
}
