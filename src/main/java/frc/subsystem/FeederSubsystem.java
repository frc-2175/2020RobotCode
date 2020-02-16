package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;

public class FeederSubsystem {

    private final SpeedController feederMotor;

    public FeederSubsystem() {
        feederMotor = new WPI_TalonSRX(9);
    }

    /**
     * ✩ roll in feeder at speed -1 ✩
     */
    public void rollInFeeder() {
        feederMotor.set(-1);
    }

    /**
     * ✩ roll out feeder at speed 1 ✩
     */
    public void rollOutFeeder() {
        feederMotor.set(1);
    }

    public void stopFeeder() {
        feederMotor.set(0);
    }
}