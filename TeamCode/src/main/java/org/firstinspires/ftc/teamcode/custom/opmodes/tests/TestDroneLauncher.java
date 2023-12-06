package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.custom.subsystems.DroneLauncher;

@TeleOp(name = "Test - Drone Launcher")
public class TestDroneLauncher extends LinearOpMode {
    public void runOpMode() {
        DroneLauncher launcher = new DroneLauncher(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                launcher.releaseDrone();
            }

            idle();
        }

        launcher.cleanup();
    }
}
