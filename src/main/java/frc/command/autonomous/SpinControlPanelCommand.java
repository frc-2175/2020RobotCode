package frc.command.autonomous;

import frc.MathUtility;
import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.ControlPanelSubsystem;

public class SpinControlPanelCommand extends Command {

    private ControlPanelSubsystem controlPanelSubsystem;
    private String goalColor;
    private String currentColor;
    private double[] redAngles = { 22.5, 202.5 };
    private double[] greenAngles = { 67.5, 247.5 };
    private double[] blueAngles = { 112.5, 292.5 };
    private double[] yellowAngles = { 157.5, 337.5 };

    public SpinControlPanelCommand(String goalColor) {
        controlPanelSubsystem = ServiceLocator.get(ControlPanelSubsystem.class);
        this.goalColor = goalColor;
    }

    public void init() {

    }

    public void execute() {
        currentColor = ControlPanelSubsystem
            .getControlPanelColor(ControlPanelSubsystem.getHue(controlPanelSubsystem.getColorSensorRed(),
            controlPanelSubsystem.getColorSensorGreen(), controlPanelSubsystem.getColorSensorBlue()));
        double currentAngle;
        if (currentColor == "red") {
            currentAngle = blueAngles[0];
        }
        if (currentColor == "green")` {
            currentAngle = yellowAngles[0];
        }
        if (currentColor == "blue") {
            currentAngle = redAngles[0];
        }
        if (currentColor == "yellow") {
            currentAngle = greenAngles[0];
        } else {
            currentAngle = 0;
        }

        double[] goalAngles;
        if (goalColor == "red") {
            goalAngles = redAngles;
        } else if (goalColor == "green") {
            goalAngles = greenAngles;
        } else if (goalColor == "blue") {
            goalAngles = blueAngles;
        } else if (goalColor == "yellow") {
            goalAngles = yellowAngles;
        } else {
            goalAngles = new double[] { 0, 0 };
        }
        double distance1 = MathUtility.getDistanceBetweenAngles(currentAngle, goalAngles[0]);
        double distance2 = MathUtility.getDistanceBetweenAngles(currentAngle, goalAngles[1]);

        double shortestDistance;
        if (Math.abs(distance1) < Math.abs(distance2)) {
            shortestDistance = distance1;
        } else {
            shortestDistance = distance2;
        }

        if (shortestDistance > 0) {
            controlPanelSubsystem.spinControlPanelForward();
        } else {
            controlPanelSubsystem.spinControlPanelBackward();
        }
    }

    public boolean isFinished() {
        return currentColor == goalColor;

    }

    public void end() {
        controlPanelSubsystem.stopSpinControlPanel();

    }
}
