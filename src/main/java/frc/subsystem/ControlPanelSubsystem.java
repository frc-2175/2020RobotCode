package frc.subsystem;

import frc.MotorWrapper;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class ControlPanelSubsystem {

    private final RobotInfo robotInfo;
    private final MotorWrapper controlPanelMotor;

    public ControlPanelSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        controlPanelMotor = robotInfo.get(RobotInfo.CONTROL_PANEL_MOTOR);
    }

    public void spinControlPanel() {
        controlPanelMotor.set(0.5);
    }

    public void stopSpinControlPanel() {
        controlPanelMotor.set(0);
    }

}