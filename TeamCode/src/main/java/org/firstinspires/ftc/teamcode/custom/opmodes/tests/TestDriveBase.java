package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.Clock;
import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;

@TeleOp(name = "Test - Drive Base")
public class TestDriveBase extends LinearOpMode {
    public void runOpMode() {
        DriveBase driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);

        waitForStart();
        while(opModeIsActive()) {

            if (gamepad1.a) {
                driveBase.dropOdometry(true);
            } else if (gamepad1.b) {
                driveBase.dropOdometry(false);
            } else if (gamepad1.start) {
                break;
            }

            if (gamepad1.back) {
                driveBase.odometry.resetEncoders();
            }

            driveBase.motorSpeedMultiplier = 0.8 - Math.min(gamepad1.left_trigger, 0.5);
            driveBase.moveSpeed(gamepad1.left_stick_y, -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x);

            driveBase.odometry.update(telemetry);

            telemetry.addData("X", driveBase.odometry.getPosition().x);
            telemetry.addData("Y", driveBase.odometry.getPosition().y);
            telemetry.addData("Heading", driveBase.odometry.getPosition().heading);

            telemetry.addLine();
            telemetry.addData("Left Raw", driveBase.odometry.getLeftEncoderTicksRaw());
            telemetry.addData("Right Raw", driveBase.odometry.getRightEncoderTicksRaw());
            telemetry.addData("Back Raw", driveBase.odometry.getBackEncoderTicksRaw());


            idle();
            telemetry.update();
        }

        driveBase.cleanup();
    }
}
