package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.subsystems.Intake;

@TeleOp(name = "Test - Intake")
public class TestIntake extends LinearOpMode {
    public void runOpMode() {
        Intake intake = new Intake(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            intake.setWristPosition(Math.abs(gamepad1.left_stick_x), Math.abs(gamepad1.right_stick_y));
            intake.closeClaws(gamepad1.a, gamepad2.b);

            if (gamepad1.dpad_left) { intake.setLeftClaw(0.0f); intake.setRightClaw(0.0f); }
            else if (gamepad1.dpad_right) { intake.setLeftClaw(0.5f); intake.setRightClaw(0.5f); }
            else if (gamepad1.dpad_down) { intake.setLeftClaw(1.0f); intake.setRightClaw(1.0f); }

            telemetry.addData("Left Pos: ", intake.getLeftClawPosition());
            telemetry.addData("Right Pos: ", intake.getRightClawPosition());

            idle();
        }

        intake.cleanup();
    }
}
