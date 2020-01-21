import edu.wpi.first.wpilibj.Timer;
import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.IntakeSubsystem;

public class IntakeCommand extends Command {

    private double timeSpinningIn;
    private IntakeSubsystem intakeSubsystem;
    private double startTime;
    private double nowTime;

    public IntakeCommand(double timeSpinningIn) {
        this.timeSpinningIn = timeSpinningIn;
        intakeSubsystem = ServiceLocator.get(IntakeSubsystem.class);
    }
    public void init() {
        startTime = Timer.getFPGATimestamp();
    }

    public void execute() {
        nowTime = Timer.getFPGATimestamp();
        intakeSubsystem.intakeRollIn();
    }

    public boolean isFinished() {
        return (nowTime - startTime) >= timeSpinningIn;
    }

    public void end() {
        intakeSubsystem.stopIntake();
    }
}