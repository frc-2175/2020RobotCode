package frc.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.RobotDriveBase.MotorType;
import frc.ServiceLocator;
import frc.info.RobotInfo;


public class ClimberSubsystem {

    private final RobotInfo robotInfo;
    private final WPI_VictorSPX deployMotor;
    private final WPI_VictorSPX climbMotor;

    public ClimberSubsystem() {
        ServiceLocator.register(this);
        robotInfo = ServiceLocator.get(RobotInfo.class);
        deployMotor = new WPI_VictorSPX(1);
        climbMotor = new WPI_VictorSPX(2);
    }

    public void delployUp() {
        deployMotor.set(1);
    } 

    public void deployDown() {
        deployMotor.set(-1);
    }

    public void stopDeploy() {
        deployMotor.set(0);
    }

    public void climbUp() {
        climbMotor.set(1);
    }

    public void climbDown() {
        climbMotor.set(-1);
    }

    public void stopClimbing() {
        climbMotor.set(0);
    }

    public void deploySpeed(double speed) {
        deployMotor.set(speed);
    }

    public void climbSpeed(double speeeed) {
        climbMotor.set(speeeed);
    }

} 