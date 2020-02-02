package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedController;

public class IntakeSubsystem {

    private final SpeedController intakeMotor;

    public IntakeSubsystem() {
        intakeMotor = new WPI_VictorSPX(9);
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