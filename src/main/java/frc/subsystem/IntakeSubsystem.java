package frc.subsystem;

import frc.MotorWrapper;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class IntakeSubsystem {

    private final RobotInfo robotInfo;
    private final MotorWrapper intakeMotor;

    public IntakeSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        intakeMotor = robotInfo.get(RobotInfo.INTAKE_MOTOR);
    }

    public void intakeRollIn() {
        intakeMotor.set(-1);
    }

    public void stopIntake() {
        intakeMotor.set(0);
    }

    public void intakeRollOut() {
        intakeMotor.set(1);
    }

    
}