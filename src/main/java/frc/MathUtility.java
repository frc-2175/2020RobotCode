package frc;

public class MathUtility {
    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }
      
    public static double getDistanceBetweenAngles(double angle1, double angle2) {
        angle1 = (angle1 % 360);
        while (angle1 < 0) {
            angle1 = angle1 + 360;
        }
        angle2 = (angle2 % 360);
        while (angle2 < 0) {
            angle2 = angle2 + 360;
        }
        double difference = angle2 - angle1;
        if (difference > 180) {
            difference = difference - 360; 
        } else if (difference < -180) {
            difference = difference + 360;
        }
        return difference; 

    }
}