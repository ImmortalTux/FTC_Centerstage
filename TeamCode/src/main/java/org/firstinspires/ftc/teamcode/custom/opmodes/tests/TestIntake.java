package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.subsystems.Intake;

@TeleOp(name = "Test - Intake")
public class TestIntake extends LinearOpMode {
    public void runOpMode() {
        Intake intake = new Intake(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            boolean updateOut = intake.update(gamepad1.left_bumper, gamepad1.right_bumper, 0);

            telemetry.addData("Left Pos", intake.getLeftClawPosition());
            telemetry.addData("Right Pos", intake.getRightClawPosition());
            telemetry.addData("Successful Comparison", updateOut);

            idle();
        }

        intake.cleanup();
    }
}
