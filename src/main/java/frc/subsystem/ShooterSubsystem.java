package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.PIDController;
import frc.ServiceLocator;

public class ShooterSubsystem {
    
    private final CANSparkMax shooterMotorMaster;
    private final CANSparkMax shooterMotorFollower;
    private final WPI_TalonSRX turretMotor;
    private final PIDController turretPidController;
    private Solenoid hoodPiston;

    private SpeedController hoodMotor;
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
        hoodMotor = new WPI_VictorSPX(9999999);
        hoodPiston = new Solenoid(888);

        double ti = 1;
        double tp = 1;
        double td = 1;
        

        turretPidController = new PIDController(tp, ti, td);
        shooterMotorFollower.follow(shooterMotorMaster);
        shooterMotorFollower.follow(shooterMotorMaster);
        ServiceLocator.register(this);
    }

    public void periodic() {
        if (currentMode == Mode.PID) { //PID!!
            CANPIDController noah2 = shooterMotorMaster.getPIDController();
            noah2.setP(.000006);
            noah2.setI(0);
            noah2.setD(0);
            noah2.setIZone(0);
            noah2.setReference(SmartDashboard.getNumber("shooter rpm", 60), ControlType.kVelocity);
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

    public void setHoodMotor(double speed) {
        hoodMotor.set(speed);
    }

    public void toggleHoodAngle() {
        if(hoodPiston.get()) {
            hoodPiston.set(false);
        } else {
            hoodPiston.set(true);
        }
    }

    public void setManualSpeed(double jacob) {
        manualSpeed = jacob;
    }
    
}


    //2 motors - on button A
    //shoot out method