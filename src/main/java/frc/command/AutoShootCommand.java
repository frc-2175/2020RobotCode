package frc.command;

import frc.ServiceLocator;
import frc.subsystem.FeederSubsystem;
import frc.subsystem.MagazineSubsystem;
import frc.subsystem.ShooterSubsystem;

public class AutoShootCommand extends Command {

    ShooterSubsystem shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class);
    FeederSubsystem feederSubsystem = ServiceLocator.get(FeederSubsystem.class);
    MagazineSubsystem magazineSubsystem = ServiceLocator.get(MagazineSubsystem.class);

	@Override
	public void init() {
		
		
	}

	@Override
	public void execute() {
        if(shooterSubsystem.nearTargetSpeed()) {
            feederSubsystem.rollInFeeder();
            magazineSubsystem.magazineRollIn();
        } else {
            feederSubsystem.stopFeeder();
            magazineSubsystem.stopmagazine();
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