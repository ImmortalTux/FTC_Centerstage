package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * @brief           Values applied when initializing OdometryEncoder objects.
 */
public class OdometryFlags {
    /**
     * @brief       Odometry wheel encoders.
     * @note        FTC library does not recognize encoders and motors as separate entities, as
     *               such, some of these variables may be used interchangeably as wheel variables.
     *               DO NOT DO THAT!! use these only as a means to gather encoder data.
     */
    public DcMotor leftOdometryEncoder, rightOdometryEncoder, backOdometryEncoder;

    /**
     * @brief       Distance between left and right odometry wheel centers.
     * @note        The units you provide here WILL determine the units in which
     *               robot position is calculated.
     * @note        Please ensure units provided here are the same format as those provided to
     *               `odometryWheelRadius`
     */
    public double wheelbaseDistance;

    /**
     * @brief       How much encoder data "increments" for every one rotation.
     */
    public int ticksPerRevolution;

    /**
     * @brief       Radius of Odometry wheels.
     * @note        The units you provide here WILL determine the units in which
     *               robot position is calculated.
     * @note        Please ensure units provided here are the same format as those provided to
     *               `wheelbaseDistance`
     */
    public double odometryWheelRadius;

    /* TODO: Documentation
       TODO: Expand on more values. */
}
