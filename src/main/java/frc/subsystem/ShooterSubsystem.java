package frc.subsystem;

import frc.MotorWrapper;
import frc.ServiceLocator;
import frc.info.RobotInfo;

public class ShooterSubsystem {
    
    private final RobotInfo robotInfo;
    private final MotorWrapper shooterMotorMaster;
    private final MotorWrapper shooterMotorFollower;


    public ShooterSubsystem() {
        robotInfo = ServiceLocator.get(RobotInfo.class);
        shooterMotorMaster = robotInfo.get(RobotInfo.SHOOTER_MOTOR_MASTER);
        shooterMotorFollower = robotInfo.get(RobotInfo.SHOOTER_MOTOR_FOLLOWER);
        shooterMotorFollower.follow(shooterMotorMaster);
    }

    public void shootOut() {
        shooterMotorMaster.set(1);
    }
    
    public void stopShootOut() {
        shooterMotorMaster.set(0);
    }
}


    //2 motors - on button A
    //shoot out method