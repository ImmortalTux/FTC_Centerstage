package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;

@TeleOp(name = "Test - Drop Odometry")
public class TestDropOdometry extends LinearOpMode {

    public void runOpMode() {
        DriveBase driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);

        waitForStart();
        while (opModeIsActive()) {}

        driveBase.cleanup();
    }
}
