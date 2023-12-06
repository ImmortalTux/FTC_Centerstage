package org.firstinspires.ftc.teamcode.custom.subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.vision.tfod.*;
import org.firstinspires.ftc.vision.VisionPortal;

public class VisionSensor extends Subsystem {
    /* TODO: Documentation */

    /**
     * @brief       Used to ensure one VisionSensor object is being used at a time.
     */
    static private Subsystem.Singleton singleton = new Subsystem.Singleton();

    final TfodProcessor processor;

    final VisionPortal portal;

    static final String[] modelLabels = {
            "Piece"
    };

    /**
     * @brief       Initializes all dependencies required by vision sensor.
     *
     * @param       map: Hardware map. This should be passed by an OpMode object.
     * @param       modelPath: Path to designated TFLite model file.
     */
    public VisionSensor(HardwareMap map, String modelPath) {
        registerSubsystem(singleton, this);;

        /* Camera initialization */
        TfodProcessor.Builder processorFlags = new TfodProcessor.Builder();
        processorFlags.setModelFileName(modelPath);
        processorFlags.setModelLabels(modelLabels);

        processor = processorFlags.build();
        processor.setZoom(1.0);

        VisionPortal.Builder portalFlags = new VisionPortal.Builder();
        portalFlags.setCamera(map.get(WebcamName.class, "VisionSensor-Webcam"));
        portalFlags.setCameraResolution(new Size(640, 480));
        portalFlags.setAutoStopLiveView(false);
        portalFlags.addProcessor(processor);

        portal = portalFlags.build();
        portal.setProcessorEnabled(processor, true);
        portal.resumeLiveView();
    }

    /**
     * @brief       Call this function whenever you are done using system object.
     */
    public void cleanup() {
        unregisterSubsystem(singleton, this);

        portal.setProcessorEnabled(processor, false);
        portal.close();
        processor.shutdown();
    }

    public void update() {

    }
}
