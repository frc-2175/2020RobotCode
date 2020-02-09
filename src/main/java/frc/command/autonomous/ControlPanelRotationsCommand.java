package frc.command.autonomous;

import frc.ServiceLocator;
import frc.command.Command;
import frc.subsystem.ControlPanelSubsystem;

public class ControlPanelRotationsCommand extends Command {
    private ControlPanelSubsystem controlPanelSubsystem;
    public ControlPanelRotationsCommand(){
        controlPanelSubsystem = ServiceLocator.get(ControlPanelSubsystem.class);
    }
    double curcumference; //Make this correct
    double motorRotations;
    public void init(){
        motorRotations = controlPanelSubsystem.controlPanelMotor.getSelectedSensorPosition();
    }
    public void execute(){
        controlPanelSubsystem.spinControlPanelForward();
        
    }
    public boolean isFinished() {
        if(controlPanelSubsystem.controlPanelMotor.getSelectedSensorPosition() - motorRotations > (128*Math.PI/curcumference)*4096 ){
            return true;
        }
        else{
            return false;
        }
    }
    public void end(){
        controlPanelSubsystem.stopSpinControlPanel();
    }
}