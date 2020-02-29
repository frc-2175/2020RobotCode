package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

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
    private double speedThreshold;
    private double goalAngle;
    private static final double OVERSHOOTNESS = 100; //the amount to add to the target speed in bbspeed calculations!!!!!
    private static final double MAX_RPM = 5620;



    public ShooterSubsystem() {
        shooterMotorMaster = new CANSparkMax(1, MotorType.kBrushless);
        shooterMotorFollower = new CANSparkMax(2, MotorType.kBrushless);
        turretMotor = new WPI_TalonSRX(2); //not accurate
        hoodMotor = new WPI_VictorSPX(99); // get value!!
        hoodPiston = new Solenoid(3); 

        double ti = 1;
        double tp = 1;
        double td = 1;
        

        SmartDashboard.putNumber("speed threshold??", 0);
        SmartDashboard.putNumber("overshootness", OVERSHOOTNESS);

        turretPidController = new PIDController(tp, ti, td);
        shooterMotorMaster.setIdleMode(IdleMode.kCoast);
        shooterMotorFollower.setIdleMode(IdleMode.kCoast);
        shooterMotorMaster.setInverted(true);
        // shooterMotorFollower.setInverted(false);
        shooterMotorFollower.follow(shooterMotorMaster, true);
        ServiceLocator.register(this);
    }

    public void periodic() {
        SmartDashboard.putNumber("flywheel speed (rpm)", getSpeedInRPM());
        speedThreshold = SmartDashboard.getNumber("speed threshold??" , speedThreshold);
        if (currentMode == Mode.PID) { //PID!!
            CANPIDController noah2 = shooterMotorMaster.getPIDController();
            noah2.setP(.000006);
            noah2.setI(0);
            noah2.setD(0);
            noah2.setIZone(0);
            noah2.setReference(SmartDashboard.getNumber("shooter rpm", 60), ControlType.kVelocity);
        } else if (currentMode == Mode.BangBang) { //Bang bang !!!!!
            if ( getSpeedInRPM() >  (speedThreshold)) {
                shooterMotorMaster.set(0);
            } else if ( getSpeedInRPM() < (speedThreshold)) {
                shooterMotorMaster.set((speedThreshold + OVERSHOOTNESS) / MAX_RPM);
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

    public void setTurretSpeed(double speed) {
        turretMotor.set(speed);
    }
    
}


    //2 motors - on button A
    //shoot out method