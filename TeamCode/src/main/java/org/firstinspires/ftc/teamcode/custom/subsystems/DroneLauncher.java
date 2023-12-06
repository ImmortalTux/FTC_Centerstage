package org.firstinspires.ftc.teamcode.custom.subsystems;

import com.qualcomm.robotcore.hardware.*;

public class DroneLauncher extends Subsystem {
    /**
     * @brief       used to ensure one Intake object is being used at a time.
     */
    private Subsystem.Singleton singleton = new Subsystem.Singleton();

    /**
     * @brief       Servos controlling right and left claws respectively.
     */
    private final Servo release;

    /**
     * @brief       Servo positions.
     */
    private final float POSITION_HOLD = 0.0f, POSITION_RELEASE = 1.0f / 15.0f;

    /**
     * @brief       Initializes all resources required by Drone Launcher.
     *
     * @param       map: Hardware map. This should be passed by an OpMode object.
     */
    public DroneLauncher(HardwareMap map) {
        registerSubsystem(singleton, this);

        release = map.servo.get("DroneLauncher-Release");
        release.setPosition(POSITION_HOLD);
    }

    /**
     * @brief       Call this function whenever you are done using system object.
     */
    public void cleanup() {
        unregisterSubsystem(singleton, this);
        release.setPosition(POSITION_HOLD);
    }

    /**
     * @brief       Releases the string to launch the airplane
     *
     */
    public void releaseDrone() {
        release.setPosition(POSITION_RELEASE);
    }
}