package frc.command;

import edu.wpi.first.wpilibj.Timer;
import frc.ServiceLocator;
import frc.subsystem.FeederSubsystem;
import frc.subsystem.MagazineSubsystem;
import frc.subsystem.ShooterSubsystem;

public class AutoShootCommand extends Command {

    ShooterSubsystem shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class);
    FeederSubsystem feederSubsystem = ServiceLocator.get(FeederSubsystem.class);
    MagazineSubsystem magazineSubsystem = ServiceLocator.get(MagazineSubsystem.class);
    double startTime;
    boolean upToSpeed;
    boolean waiting = true;
    

	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
        if ( waiting ) {
            if ( shooterSubsystem.nearTargetSpeed() ) {
                waiting = false;
                startTime = Timer.getFPGATimestamp();
            }
        } else {
            if(Timer.getFPGATimestamp() - startTime < .5) {
                feederSubsystem.rollInFeeder();
            } else if(Timer.getFPGATimestamp() - startTime < 1) {
                magazineSubsystem.magazineRollIn();
            } else {
                 waiting = true;
            }
        }





	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end() {
        feederSubsystem.stopFeeder();
        magazineSubsystem.stopmagazine();
	}
}