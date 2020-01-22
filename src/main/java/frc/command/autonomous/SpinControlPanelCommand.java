import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.ControlPanelSubsystem;

public class SpinControlPanelCommand extends Command {
        private ControlPanelSubsystem controlPanelSubsystem;

    public SpinControlPanelCommand() {
        controlPanelSubsystem = ServiceLocator.get(ControlPanelSubsystem.class);
    }
    public void init() {
        

    }
    public void execute() {

    }
    public boolean isFinished() {

    }
    public void end() {

    }
}
