package frc.command.autonomous;

import frc.command.Command;
import frc.ServiceLocator;
import frc.subsystem.ShooterSubsystem;
import frc.subsystem.VisionSubsystem;

public class AimTurretWithVisionCommand extends Command {
    private ShooterSubsystem shooterSubsystem; 
    private VisionSubsystem visionSubsystem; 

    public AimTurretWithVisionCommand() {
        shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class); 
        visionSubsystem = ServiceLocator.get(VisionSubsystem.class);
    }

    public void init() {
        shooterSubsystem.setGoalAngle(visionSubsystem.getLimelightHorizontalOffset());
    }

    public void execute() {
        shooterSubsystem.turretPIDToGoalAngle();
    }

    public boolean isFinished() {
        return false; 
    }

    public void end() {
        shooterSubsystem.setTurretSpeed(0);
    }

}