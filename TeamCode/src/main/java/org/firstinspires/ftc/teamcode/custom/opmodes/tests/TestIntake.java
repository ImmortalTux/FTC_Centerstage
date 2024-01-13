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

            telemetry.addLine();
            telemetry.addLine("Left Sensor");
            telemetry.addData("Red", intake.getLeftPixelConfirmation().red());
            telemetry.addData("Green", intake.getLeftPixelConfirmation().green());
            telemetry.addData("Blue", intake.getLeftPixelConfirmation().blue());
            telemetry.addData("Alpha", intake.getLeftPixelConfirmation().alpha());

            telemetry.addLine();
            telemetry.addLine("Right Sensor");

            telemetry.addData("Red", intake.getRightPixelConfirmation().red());
            telemetry.addData("Green", intake.getRightPixelConfirmation().green());
            telemetry.addData("Blue", intake.getRightPixelConfirmation().blue());
            telemetry.addData("Alpha", intake.getRightPixelConfirmation().alpha());

            idle();
        }

        intake.cleanup();
    }
}
