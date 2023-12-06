package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.custom.Clock;
import org.firstinspires.ftc.teamcode.custom.subsystems.Lift;


@TeleOp(name = "Test - Lift")
public class TestLift extends LinearOpMode {
    private Lift lift = null;

    final int ARM_TICK_SPEED = 500;
    final int LIFT_TICK_SPEED = 1000;

    public void runOpMode() {
        lift = new Lift(hardwareMap);

        int moveUpInputBuffer = 0;
        int moveOutInputBuffer = 0;

        waitForStart();
        Clock.init();
        while (opModeIsActive()) {
            Clock.updateDeltaTime();
//            lift.update();

            moveUpInputBuffer += gamepad1.dpad_up ? LIFT_TICK_SPEED * Clock.getDeltaTime() : 0;
            moveUpInputBuffer -= gamepad1.dpad_down ? LIFT_TICK_SPEED * Clock.getDeltaTime() : 0;
            lift.setLiftPosition(moveUpInputBuffer);
//            lift.update();

            moveOutInputBuffer += gamepad1.dpad_right ? ARM_TICK_SPEED * Clock.getDeltaTime() : 0;
            moveOutInputBuffer -= gamepad1.dpad_left ? ARM_TICK_SPEED * Clock.getDeltaTime() : 0;
            lift.setArmPosition(moveOutInputBuffer);

            telemetry.addData("Up Buf", moveUpInputBuffer);
            telemetry.addData("Out Buf", moveOutInputBuffer);
            telemetry.addLine();
            telemetry.addData("Lift pos", lift.getLiftMotorTicks());
            telemetry.addData("Arm pos", lift.getArmMotorTicks());
            telemetry.addLine();

            telemetry.addData("Lift Target: ", lift.getLiftTargetPosition());
            telemetry.addData("Lift Target (Abs): ", Math.abs(lift.getLiftTargetPosition()));
            telemetry.addData("Lift Current Pos: ", lift.getLiftMotorTicks());

            telemetry.addLine();
            telemetry.addData("Delta Time: ", Clock.getDeltaTime());
            telemetry.addData("Delta Time * Tick Speed: ", LIFT_TICK_SPEED * Clock.getDeltaTime());

            telemetry.update();

            idle();
        }

        lift.cleanup();
    }
}
