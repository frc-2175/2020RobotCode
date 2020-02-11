package frc.subsystem;

import frc.ServiceLocator;
import frc.MotorWrapper;

import frc.info.RobotInfo;

public class ClimberSubsystem {
    
    private final RobotInfo robotInfo;
    private final MotorWrapper climberMotor;
    private final MotorWrapper winchMotor;
    private double climbingSpeed;
    private double winchSpeed;

    public ClimberSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        climberMotor = robotInfo.get(RobotInfo.CLIMBER_MOTOR);
        winchMotor = robotInfo.get(RobotInfo.WINCH_MOTOR);
    }

    public void climbUp() {
        climberMotor.set(1);
    } 

    public void climbDown() {
        climberMotor.set(-1);
    }

    public void stopClimbing() {
        climberMotor.set(0);
    }

    public void winchIn() {
        winchMotor.set(1);
    }

    public void winchOut() {
        winchMotor.set(-1);
    }

    public void stopWinching() {
        winchMotor.set(0);
    }

    public void climbingSpeed(double speed) {
        climberMotor.set(speed);
    }

    public void winchSpeed(double speeed) {
        winchMotor.set(speeed);
    }

}