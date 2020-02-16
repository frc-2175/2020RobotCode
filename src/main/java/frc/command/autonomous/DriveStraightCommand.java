package frc.command.autonomous;

import edu.wpi.first.wpilibj.Timer;
import frc.PIDController;
import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.DrivetrainSubsystem;

public class DriveStraightCommand extends Command {
    double distance;
    DrivetrainSubsystem drivetrainSubsystem;
    double initialDistance;
    double initialHeading;
    double currentDistance;
    double currentHeading;
    double speed;
    double p = 1.0/45.0; // TODO : find better values for these
    double i = 0;
    double d = 0;
    PIDController pidController;

    public DriveStraightCommand(double distance, double speed) { // TODO : this is in ticks right now i think??? so we
                                                                 // should fix that !!!!!
        this.distance = distance;
        this.speed = speed;
        drivetrainSubsystem = ServiceLocator.get(DrivetrainSubsystem.class);
    }

    public void init() {
        initialHeading = drivetrainSubsystem.getHeading();
        initialDistance = drivetrainSubsystem.getAverageEncoderDistance();
        pidController = new PIDController(p, i, d);
    }

    public void execute() {

        currentDistance = drivetrainSubsystem.getAverageEncoderDistance();
        currentHeading = drivetrainSubsystem.getHeading();

        if (distance > 0) {
            drivetrainSubsystem.blendedDrive(speed, pidController.pid(currentHeading, initialHeading));
        } else {
            drivetrainSubsystem.blendedDrive(-speed, pidController.pid(currentHeading, initialHeading));
        }

        pidController.updateTime(Timer.getFPGATimestamp());
    }

    public boolean isFinished() {
        if (Math.abs(initialDistance - currentDistance) >= Math.abs(distance)) {
            return true;
        } else {
            return false;
        }
    }

    public void end() {
        drivetrainSubsystem.stopAllMotors();
    }
}
