package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.PIDController;
import frc.ServiceLocator;

public class ShooterSubsystem {
    
    private final CANSparkMax flywheelMotorMaster;
    private final CANSparkMax flywheelMotorFollower;
    private final PIDController pidController; 
    double conversionNumber = 4096;
 
    public ShooterSubsystem() {
        flywheelMotorMaster = new CANSparkMax(7, MotorType.kBrushless);
        flywheelMotorFollower = new CANSparkMax(8, MotorType.kBrushless);
        double kp = 1.0 / 36.0;
        double ki = 1.0 / 30.0;
        double kd = 0;
        pidController = new PIDController(kp, ki, kd);
        
        flywheelMotorFollower.follow(flywheelMotorMaster);
        ServiceLocator.register(this);

    }

    public void shootOut() {
        flywheelMotorMaster.set(1);
    }
    
    public void stopShootOut() {
        flywheelMotorMaster.set(0);
    }

    /**
     * gives current speed in revolutions per minute!!
     * @return current speed in rpm
     */
    public double convertToRPM() {
        return flywheelMotorMaster.getEncoder().getVelocity(); // TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!S
        /* double ticksPerSecond = originalSpeed * 10;
        double ticksPerMinute = ticksPerSecond * 60;
        return ticksPerMinute / conversionNumber; //revolutions per minute */
    }

    public double pidSpeed() { //TODO
        return 0;
    }
}


    //2 motors - on button A
    //shoot out method