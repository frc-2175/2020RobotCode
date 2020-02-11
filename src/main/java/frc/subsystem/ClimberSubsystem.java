package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.RobotDriveBase.MotorType;
import frc.ServiceLocator;
import frc.info.RobotInfo;


public class ClimberSubsystem {

    private final RobotInfo robotInfo;
    private final WPI_VictorSPX climberMotor;
    private final WPI_VictorSPX winchMotor;
    private double climbingSpeed;
    private double winchSpeed;

    public ClimberSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        climberMotor = new WPI_VictorSPX(1);
        winchMotor = new WPI_VictorSPX(2);
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