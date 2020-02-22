package frc.command.autonomous;

import edu.wpi.first.wpilibj.Timer;
import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.ShooterSubsystem;

public class ShootCommand extends Command {

    private double shootingTime;
    private ShooterSubsystem shooterSubsystem;
    double startTime;

    public ShootCommand(double shootingTime) {
        this.shootingTime = shootingTime;
        shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class);
    }

    public void init() {
        startTime = Timer.getFPGATimestamp();
    }

    public void execute() {
        //shooterSubsystem.shootOut();
    }

    public boolean isFinished() {
        if (Timer.getFPGATimestamp() - startTime > shootingTime) {
            return true;
        } else {
            return false;
        }
    }

    public void end() {
        //shooterSubsystem.stopShootOut();
    }
}