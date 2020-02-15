package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class MagazineSubsystem {

    private final SpeedController magazineMotor;

    public MagazineSubsystem() {
        magazineMotor = new WPI_VictorSPX(10);
    }

    /**
     * rolls intake in at full in
     */
    public void magazineRollIn() {
        magazineMotor.set(-1);
    }

    public void stopmagazine() {
        magazineMotor.set(0);
    }

    /**
     * ✩ Actually just sets speed of magazine motor ✩
     * @param speed speed to set magazine motor to
     */
    public void setMagazineMotor(double speed) {
        magazineMotor.set(speed);
    }

    /**
     * rolls intake out at full speed
     */
    public void magazineRollOut() {
        magazineMotor.set(1);
    }

    
}