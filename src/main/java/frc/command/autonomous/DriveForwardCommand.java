package frc.command.autonomous;

import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.DrivetrainSubsystem;

public class DriveForwardCommand extends Command {
    double distance;
    DrivetrainSubsystem drivetrainSubsystem;
    
    public DriveForwardCommand(double distance, int i) { //TODO : this is in ticks right now i think??? so we should fix that !!!!!
        this.distance = distance;
        drivetrainSubsystem = ServiceLocator.get(DrivetrainSubsystem.class);
    }
    public void init() {
        drivetrainSubsystem.resetTracking();
    }
    public void execute() {
        drivetrainSubsystem.tankDrive( .6, .6);
    }
    public boolean isFinished() {
        if(drivetrainSubsystem.getAverageEncoderDistance() >= distance) {
            return true;
        } else {
            return false;
        }
    }

    public void end() {
        drivetrainSubsystem.stopAllMotors();

    }
}

