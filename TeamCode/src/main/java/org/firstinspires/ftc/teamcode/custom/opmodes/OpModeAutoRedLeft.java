package org.firstinspires.ftc.teamcode.custom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.custom.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.custom.subsystems.Lift;
import org.firstinspires.ftc.teamcode.custom.subsystems.Intake;

import org.firstinspires.ftc.teamcode.custom.Clock;

@Autonomous(name = "OpMode - Autonomous - Red Left")
public class OpModeAutoRedLeft extends OpMode {
    private DriveBase driveBase = null;
    private Lift lift = null;
    private Intake intake = null;

    private int currentStage;
    private int errorRange;

    /**
     * @brief       Code ran to initialize robot.
     */
    @Override
    public void init() {
        driveBase = new DriveBase(hardwareMap);
        driveBase.dropOdometry(true);
        driveBase.motorSpeedMultiplier = 0.3;
        driveBase.odometry.resetEncoders();

        lift = new Lift(hardwareMap);

        intake = new Intake(hardwareMap);
        intake.closeClaws(true, true);

        currentStage = 0;
        errorRange = 10;
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
    public void start() {
        Clock.init();
    }

    /**
     * @brief       Main robot loop. All driving code goes here.
     */
    @Override
    public void loop() {
        Clock.updateDeltaTime();
        driveBase.odometry.update(telemetry);



        telemetry.update();
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
