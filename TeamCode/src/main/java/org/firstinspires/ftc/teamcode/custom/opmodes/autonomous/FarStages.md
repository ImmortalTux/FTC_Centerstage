# Stages (FAR):

- Initialization: Close claws, raise arm, check where prop is. S1, S2, and S3 are no longer needed. Make sure Drive Team knows to wait until the object is found.
- S1: Lower arm to 0.
- S2: Drive to center of the three tape lines
- S3: Pivot left (-90 deg), right (90 deg), or not at all depending on the specified position interpreted by the camera.
- S4: Drive forward slightly and align the claw with the tape.
    - Open the claws
    - Slightly raise the arm to leave the colored pixel on the tape.
- S5: Drive around the tape markers.
- S6: Drive all the way to the backstage area (across the entire field!)
    - Drive up to the red/blue tape marker surrounding the stage area.
- S7: Slowly strafe right to give the camera time to scan the AprilTags.
    - Stop whenever the correct AprilTag is seen.
- S8: Raise the claws so they are at a 60 deg angle relative to the floor.
    - Need special code to make sure it is actually 60 degs, but we have some free room in this angle.
- S9: Drive forward slowly so the claw is pressed flush against the stage wall.
- S10: Open the claws.
- S11: Back away a very small amount so the pixels are free to fall but we are still inside the backstage area.
    - We can get both placing and parking points through this.