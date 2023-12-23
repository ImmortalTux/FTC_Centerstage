package org.firstinspires.ftc.teamcode.custom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.checkerframework.checker.units.qual.Current;
import org.firstinspires.ftc.teamcode.custom.Clock;
import org.firstinspires.ftc.teamcode.custom.subsystems.*;

@Autonomous(name = "Autonomous Testing")
public class TestAutoStuff extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;

    // An error range of ±3150 ticks.
    private int TargetError = 3150;

    private int CurrentStage = 0;

    /**
     * @brief       Code run once to initialize robot.
     */
    @Override
    public void init() {
        driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);

        lift = new Lift(hardwareMap);

        intake = new Intake(hardwareMap);
        intake.closeClaws(true, true);
    }

    /**
     * @brief       Code ran repeatedly once robot is initialized. This will automatically stop
     *              execution once user hits PLAY button.
     */
    @Override
    public void init_loop() {

    }

    /**
     * @brief       Ran once driver hits PLAY button.
     */
    @Override
    public void start() { Clock.init(); }

    /**
     * @brief       Main robot loop. All driving code goes here.
     */
    @Override
    public void loop() {
        if (CurrentStage == 0) {

        } else if (CurrentStage == 1) {

        } else if (CurrentStage == 2) {

        } else if (CurrentStage == 3) {

        } else if (CurrentStage == 4) {

        } else if (CurrentStage == 5) {

        } else if (CurrentStage == 6) {

        } else if (CurrentStage == 7) {

        } else if (CurrentStage == 8) {

        } else if (CurrentStage == 9) {

        } else if (CurrentStage == 10) {

        } else if (CurrentStage == 11) {

        } else if (CurrentStage == 12) {

        } else if (CurrentStage == 13) {

        } else {

        }
    }

    /**
     * @brief       Ran once robot is told to stop. Cleanup code goes here.
     */
    @Override
    public void stop() {
        driveBase.cleanup();
        lift.cleanup();
        intake.cleanup();
    }
}
