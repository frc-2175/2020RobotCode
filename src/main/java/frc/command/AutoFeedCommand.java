package frc.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ServiceLocator;
import frc.subsystem.FeederSubsystem;
import frc.subsystem.MagazineSubsystem;
import frc.subsystem.ShooterSubsystem;

public class AutoFeedCommand extends Command {

    ShooterSubsystem shooterSubsystem = ServiceLocator.get(ShooterSubsystem.class);
    FeederSubsystem feederSubsystem = ServiceLocator.get(FeederSubsystem.class);
    MagazineSubsystem magazineSubsystem = ServiceLocator.get(MagazineSubsystem.class);
    double startTime;
    boolean upToSpeed;
    boolean waiting = true;

    public AutoFeedCommand() {
        SmartDashboard.putNumber("magazine time", .10);
        SmartDashboard.putNumber("feeder time", .50);
    }

	@Override
	public void init() {
		startTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
        double magazineTime = SmartDashboard.getNumber("magazine time", .07);
        double feederTime = SmartDashboard.getNumber("feeder time", .25);
        if ( waiting ) {
            if ( shooterSubsystem.nearTargetSpeed() ) {
                waiting = false;
                startTime = Timer.getFPGATimestamp();
            }
        } else {
            if(Timer.getFPGATimestamp() - startTime < feederTime) {
                feederSubsystem.rollUp();
            } else if(Timer.getFPGATimestamp() - startTime < feederTime + magazineTime) {
                feederSubsystem.stopFeeder();
                magazineSubsystem.setMagazineMotor(.87);
            } else {
                magazineSubsystem.stopmagazine();
                feederSubsystem.stopFeeder();
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