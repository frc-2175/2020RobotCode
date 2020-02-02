package frc.math;

public class MathUtility {
    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }
      
    public static double getDistanceBetweenAngles(double startAngle, double endAngle) {
        startAngle = (startAngle % 360);
        while (startAngle < 0) {
            startAngle = startAngle + 360;
        }
        endAngle = (endAngle % 360);
        while (endAngle < 0) {
            endAngle = endAngle + 360;
        }
        double difference = endAngle - startAngle;
        if (difference > 180) {
            difference = difference - 360; 
        } else if (difference < -180) {
            difference = difference + 360;
        }
        return difference; 

    }
}