package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.PIDController;

public class ShooterSubsystem {
    
    private final CANSparkMax shooterMotorMaster;
    private final CANSparkMax shooterMotorFollower;
    private final PIDController pidController; 
    double conversionNumber = 4096;

    public ShooterSubsystem() {
        shooterMotorMaster = new CANSparkMax(7, MotorType.kBrushless);
        shooterMotorFollower = new CANSparkMax(8, MotorType.kBrushless);
        double kp = 1.0 / 36.0;
        double ki = 1.0 / 30.0;
        double kd = 0;
        pidController = new PIDController(kp, ki, kd);
        
        shooterMotorFollower.follow(shooterMotorMaster);

    }

    public void shootOut() {
        shooterMotorMaster.set(1);
    }
    
    public void stopShootOut() {
        shooterMotorMaster.set(0);
    }

    /**
     * gives current speed in revolutions per minute!!
     * @return current speed in rpm
     */
    public double convertToRPM() {
        double originalSpeed = shooterMotorMaster.get(); // TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!S
        double ticksPerSecond = originalSpeed * 10;
        double ticksPerMinute = ticksPerSecond * 60;
        return ticksPerMinute / conversionNumber; //revolutions per minute
    }

    public double pidSpeed() { //TODO
        return 0;
    }
}


    //2 motors - on button A
    //shoot out method