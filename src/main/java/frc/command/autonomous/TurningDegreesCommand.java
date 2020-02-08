package frc.command.autonomous;

import frc.command.Command;
import frc.subsystem.DrivetrainSubsystem;
import frc.ServiceLocator;

public class TurningDegreesCommand extends Command {
    double turnDegrees;
    DrivetrainSubsystem driveTrainSubsystem;
    double initialDegrees;
    double degreesTurned;
    

    public TurningDegreesCommand(double turnDegrees) {
        this.turnDegrees = turnDegrees;
        driveTrainSubsystem = ServiceLocator.get(DrivetrainSubsystem.class);
    }

    public void init() {
        initialDegrees = driveTrainSubsystem.getHeading(); 
    }

    public void execute() {
        if (turnDegrees > 0) {
            driveTrainSubsystem.blendedDrive(0, .7);
        } else if (turnDegrees < 0) {
            driveTrainSubsystem.blendedDrive(0, -.7);
        } else {
            driveTrainSubsystem.blendedDrive(0, 0);
        }
        degreesTurned = driveTrainSubsystem.getHeading() - initialDegrees;
        //how many degrees you've turned = current degrees turned - the starting number of degrees
    }

    public boolean isFinished() {
        if (turnDegrees > 0) {
            if (degreesTurned >= turnDegrees) {
                return true;
            } else {
                return false;
            }
        } else if (turnDegrees < 0) {
            if (degreesTurned <= turnDegrees) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    } public void end () {
        driveTrainSubsystem.blendedDrive(0, 0);
    }
}

