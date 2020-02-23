package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.PIDController;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class ShooterSubsystem {
    
    private final CANSparkMax shooterMotorMaster;
    private final CANSparkMax shooterMotorFollower;
    private final WPI_TalonSRX turretMotor;
    private final PIDController shooterWheelPidController; 
    private final PIDController turretPidController;
    public enum Mode {Manual, PID, BangBang};
    private Mode currentMode = Mode.Manual;
    private double goalSpeedRPM;
    private double manualSpeed = 0;
    private double speedThreshold = 5;
    private double speedRange = 5;
    private double goalAngle;


    public ShooterSubsystem() {
        shooterMotorMaster = new CANSparkMax(7, MotorType.kBrushless);
        shooterMotorFollower = new CANSparkMax(8, MotorType.kBrushless);
        turretMotor = new WPI_TalonSRX(10); //not accurate
        double sp = 1.0 / 36.0;
        double si = 1.0 / 30.0;
        double sd = 0;
        double ti = 1;
        double tp = 1;
        double td = 1;
        
        shooterWheelPidController = new PIDController(sp, si, sd);
        turretPidController = new PIDController(tp, ti, td);
        
        shooterMotorFollower.follow(shooterMotorMaster);
        ServiceLocator.register(this);
    }

    public void periodic() {
        if (currentMode == Mode.PID) { //PID!!
            shooterMotorMaster.set(shooterMotorMaster.get() + shooterWheelPidController.pid(getSpeedInRPM(), goalSpeedRPM));
        } else if (currentMode == Mode.BangBang) { //Bang bang !!!!!
            if ( getSpeedInRPM() >  (speedThreshold + speedRange)) {
                shooterMotorMaster.set(0);
            } else if ( getSpeedInRPM() < (speedThreshold - speedRange)) {
                shooterMotorMaster.set(1);
            } 
        } else { //do manual stuff!!!!!!!!
            shooterMotorMaster.set(manualSpeed);
        }
    }

    public double getSpeedInRPM() {
        return shooterMotorMaster.getEncoder().getVelocity();
    }

    public void setMode(Mode noah) {
        currentMode = noah;
    }

    public void setManualSpeed(double jacob) {
        manualSpeed = jacob;
    }
}


    //2 motors - on button A
    //shoot out method