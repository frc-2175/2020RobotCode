package frc.subsystem;

import frc.math.Vector;

public class VisionSubsystem {
    private static final double cameraAngle = Math.toRadians(20);
    private static final double cameraHeight = 10;
    private static final double targetWidth = 39.25;
    private static final double targetHeight = 17;
    private static final double targetBottomHeightFromFloor = 81.25;
    private static final double targetTopHeightFromFloor = targetBottomHeightFromFloor + targetHeight;


    public double getDistanceFromTarget(double pixelAngleY) {
        double angle = pixelAngleY + cameraAngle;
        return (targetHeight - cameraHeight) / Math.tan(angle);
    }

    public Vector getTargetPositionInCameraSpace(double pixelAngleX, double pixelAngleY) {
        double distance = getDistanceFromTarget(pixelAngleY);
        double yPosition = Math.sin(pixelAngleX) * distance;
        double xPosition = Math.cos(pixelAngleX) * distance;
        return new Vector(xPosition, yPosition);
    }
}