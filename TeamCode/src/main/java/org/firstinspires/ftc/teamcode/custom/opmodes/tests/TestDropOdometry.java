package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;

@TeleOp(name = "Test - Drop Odometry Wheels")
public class TestDropOdometry extends LinearOpMode {
    DriveBase driveBase = null;

    @Override
    public void runOpMode() {
        driveBase = new DriveBase(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            driveBase.dropOdometry(true);
        }

        driveBase.cleanup();
    }
}
