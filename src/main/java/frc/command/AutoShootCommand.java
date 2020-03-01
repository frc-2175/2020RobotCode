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
        // double howLongHasItBeen = Timer.getFPGATimestamp() - startTime;
        // if(shooterSubsystem.nearTargetSpeed()) { //is it up to speed??
        //     // if(howLongHasItBeen < 2.5) {
        //     //     feederSubsystem.rollInFeeder();
        //     // } else if (howLongHasItBeen < 5) {
        //     //     magazineSubsystem.magazineRollIn();
        //     // } else {
        //     //     startTime = Timer.getFPGATimestamp();
        //     // }

        //     feederSubsystem.rollInFeeder();
        //     magazineSubsystem.stopmagazine();
        // } else {
        //     feederSubsystem.stopFeeder();
        //     // magazineSubsystem.stopmagazine();
        //     magazineSubsystem.magazineRollIn();
        // }
        // upToSpeed = shooterSubsystem.nearTargetSpeed();

        // if(upToSpeed) {
        //     upToSpeed = true;
        //     if(Timer.getFPGATimestamp() - startTime > 100000000) {
        //         feederSubsystem.rollInFeeder();
        //     } else if(Timer.getFPGATimestamp() - startTime > 2000000) {
        //         magazineSubsystem.magazineRollIn();
        //     } else {
        //         upToSpeed = false;
        //         startTime = Timer.getFPGATimestamp();
        //     }
        // }

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