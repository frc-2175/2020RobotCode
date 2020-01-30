package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.MotorWrapper;
import frc.PIDController;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class ShooterSubsystem {
    
    private final RobotInfo robotInfo;
    private final MotorWrapper shooterMotorMaster;
    private final MotorWrapper shooterMotorFollower;
    private final PIDController pidController; 
    double conversionNumber = 4096;
    public enum Mode {Manual, PID, BangBang};
    private Mode currentMode = Mode.Manual;
    private double goalSpeedRPM;
    private double manualSpeed = 0;


    public ShooterSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        shooterMotorMaster = robotInfo.get(RobotInfo.SHOOTER_MOTOR_MASTER);
        shooterMotorFollower = robotInfo.get(RobotInfo.SHOOTER_MOTOR_FOLLOWER);
        double kp = 1.0 / 36.0;
        double ki = 1.0 / 30.0;
        double kd = 0;
        
        pidController = new PIDController(kp, ki, kd);
        
        shooterMotorFollower.follow(shooterMotorMaster);

        shooterMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        shooterMotorMaster.setSelectedSensorPosition(0, 0, 0);
    }

    public void periodic() {
        if (currentMode == Mode.PID) { //PID!!
            shooterMotorMaster.set(shooterMotorMaster.get() + pidController.pid(getSpeedInRPM(), goalSpeedRPM));
        } else if (currentMode == Mode.BangBang) { //Bang bang !!!!!

        } else { //do manual stuff!!!!!!!!
            shooterMotorMaster.set(manualSpeed);
        }
    }

    public void shootOut() {
        shooterMotorMaster.set(1);
    }
    
    public void stopShootOut() {
        shooterMotorMaster.set(0);
    }

    public double convertToRPM(double originalSpeed) {
        double ticksPerSecond = originalSpeed * 10;
        double ticksPerMinute = ticksPerSecond * 60;
        return ticksPerMinute / conversionNumber; //revolutions per minute
    }

    public double getSpeedInRPM() {
        double curSpeed = shooterMotorMaster.getSelectedSensorVelocity();
        return convertToRPM(curSpeed);
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