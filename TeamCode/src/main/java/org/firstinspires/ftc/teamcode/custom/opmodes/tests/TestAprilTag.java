package org.firstinspires.ftc.teamcode.custom.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Test - AprilTag Detection")
public class TestAprilTag extends LinearOpMode {
  private AprilTagProcessor aprilTagProcessor = null;

  public void runOpMode() {
    aprilTagProcessor = new AprilTagProcessor.builder()
      .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());
      .build();
    
    waitForStart();
    while (opModeIsActive()) {
      for (AprilTagDetection detection : aprilTagProcessor.getDetections()) {
        Orientation rotation = Orientation.getOrientation(detection.rawPose.R, AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double poseX = detection.rawPose.x;
        double poseY = detection.rawPose.y;
        double poseZ = detection.rawPose.z;

        double poseAX = rot.firstAngle;
        double poseAY = rot.secondAngle;
        double poseAZ = rot.thirdAngle;
      }
    }
  }
}
