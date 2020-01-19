package frc.subsystem;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import frc.MotorWrapper;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class ControlPanelSubsystem {

    private final RobotInfo robotInfo;
    private final MotorWrapper controlPanelMotor;
    private final ColorSensorV3 colorSensor; 
    public static double[] controlPanelCyan = {0, 255, 255};
    public static double[] controlPanelGreen = {0, 255, 0};
    public static double[] controlPanelRed = {255, 0, 0};
    public static double[] controlPanelYellow = {255, 255, 0};


    public ControlPanelSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        controlPanelMotor = robotInfo.get(RobotInfo.CONTROL_PANEL_MOTOR);
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    }

    public void spinControlPanel() {
        controlPanelMotor.set(0.5);
    }

    public void stopSpinControlPanel() {
        controlPanelMotor.set(0);
    }

    public double getColorSensorRed() {
        return colorSensor.getColor().red;
    } 

    public double getColorSensorBlue() {
        return colorSensor.getColor().blue;
    }

    public double getColorSensorGreen() {
        return colorSensor.getColor().green;
    }

    public double getColorSensorIR() {
        return colorSensor.getIR();
    }

    public double getColorSensorProximity() {
        return colorSensor.getProximity();
    }

    public String getControlPanelColor(double red, double green, double blue) {

    }

    public static double getHue(double red, double green, double blue) {
        double cMax = Math.max(Math.max(red, green), blue);
        double cMin = Math.min(Math.min(red, green), blue);
        double delta = cMax - cMin;
        System.out.println("cMax = " + cMax + " cMin = " + cMin + " delta = " + delta);
    
        if (delta == 0) {
          System.out.println("0");
          return 0;
        } else if (cMax == red) {
          System.out.println("red");
          return 60 * mod(((green - blue) / delta), 6);
        } else if (cMax == green) {
          System.out.println("green");
          return 60 * (((blue - red) / delta) + 2);
        } else if (cMax == blue) {
          System.out.println("blue");
          return 60 * (((red - green) / delta) + 4);
        } else {
          return 0;
        }
      } 

      public static double mod(double a, double b) {
        return ((a % b) + b) % b;
      }
}