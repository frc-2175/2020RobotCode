package frc.subsystem;

import frc.math.Vector;

public class VisionSubsystem {
    private static final double cameraAngle = Math.toRadians(20);
    private static final double cameraHeight = 10;
    private static final double targetWidth = 39.25;
    private static final double targetHeight = 17;
    private static final double targetBottomHeightFromFloor = 81.25;
    private static final double targetTopHeightFromFloor = targetBottomHeightFromFloor + targetHeight;


    public double getDistanceFromTarget(double pixelAngleY, double height) {
        double angle = pixelAngleY + cameraAngle;
        return (height - cameraHeight) / Math.tan(angle);
    }

    public Vector getPixelPositionInCameraSpace(double pixelAngleX, double pixelAngleY, double height) {
        double distance = getDistanceFromTarget(pixelAngleY, height);
        double yPosition = Math.sin(pixelAngleX) * distance;
        double xPosition = Math.cos(pixelAngleX) * distance;
        return new Vector(xPosition, yPosition);
    }

    public Vector getTargetPositionInCameraSpace(
        Vector topLeftCorner,
        Vector topRightCorner, 
        Vector bottomLeftCorner,
        Vector bottomRightCorner
    ) {
        Vector topLeftCornerPosition = getPixelPositionInCameraSpace(topLeftCorner.x, topLeftCorner.y, targetTopHeightFromFloor);
        Vector topRightCornerPosition = getPixelPositionInCameraSpace(topRightCorner.x, topRightCorner.y, targetTopHeightFromFloor);
        Vector bottomRightCornerPosition = getPixelPositionInCameraSpace(bottomRightCorner.x, bottomRightCorner.y, targetBottomHeightFromFloor);
        Vector bottomLeftCornerPosition = getPixelPositionInCameraSpace(bottomLeftCorner.x, bottomLeftCorner.y, targetBottomHeightFromFloor);
        
        double centerXPosition = (topLeftCornerPosition.x + topRightCornerPosition.x + bottomLeftCornerPosition.x + bottomRightCornerPosition.x)/4 ; 
        double centerYPosition = (topLeftCornerPosition.y + topRightCornerPosition.y + bottomLeftCornerPosition.y + bottomRightCornerPosition.y)/4 ; 
       
        return new Vector(centerXPosition, centerYPosition);
    }
}