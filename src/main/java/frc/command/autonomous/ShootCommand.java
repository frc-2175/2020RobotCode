package frc.command.autonomous;

import edu.wpi.first.wpilibj.Timer;
import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.ShooterSubsystem;

// public class ShootCommand extends Command {
//     private double shootingTime;
//     private ShooterSubsystem shooterSubsystem;
//     public ShootCommand (double shootingTime) {
//         this.shootingTime = shootingTime;
//         shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class);
//     }
//     @Override
//     public void init () {
        
//     }
//     @Override
//     public void execute () {
//         shooterSubsystem.shootOut();
//     }
//     public boolean isFinished () {
//         if (Timer.getFPGATimestamp() )
//     }
// }