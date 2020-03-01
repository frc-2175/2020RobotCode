package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.ServiceLocator;
import frc.info.RobotInfo;


public class ClimberSubsystem {

    private final RobotInfo robotInfo;
    private final WPI_TalonSRX deployMotor;
    private final WPI_VictorSPX climbMotor;
    public static final double CLIMBER_MAX_EXTENSION_ROTATIONS = 7.6595744;

    public ClimberSubsystem() {
        ServiceLocator.register(this);
        robotInfo = ServiceLocator.get(RobotInfo.class);
        deployMotor = new WPI_TalonSRX(6);
        deployMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        climbMotor = new WPI_VictorSPX(5);
    }

    public void deployUp() {
        if(deployMotor.getSelectedSensorPosition() / 4096.0 < CLIMBER_MAX_EXTENSION_ROTATIONS) {
            deployMotor.set(1);
        }
    } 

    public void deployDown() {
        deployMotor.set(-.4);
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
} 