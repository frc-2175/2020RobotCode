package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class IntakeSubsystem {

    private final SpeedController intakeMotor;
    private final DoubleSolenoid intakePiston;

    public IntakeSubsystem() {
        intakeMotor = new WPI_TalonSRX(10);
        intakePiston = new DoubleSolenoid(0, 1);
        
        intakeMotor.setInverted(true);
    }

    /**
     * rolls intake in at full in
     */
    public void intakeRollIn() {
        intakeMotor.set(-1);
    }

    public void stopIntake() {
        intakeMotor.set(0);
    }

    public void toggleIntake() {
        if ( intakePiston.get() == DoubleSolenoid.Value.kForward) {
            intakePiston.set(DoubleSolenoid.Value.kReverse);
        } else {
            intakePiston.set(DoubleSolenoid.Value.kForward);
        }
    }

    /**
     * rolls intake out at full speed
     */
    public void intakeRollOut() {
        intakeMotor.set(1);
    }

    
}