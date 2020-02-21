package frc.command.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ServiceLocator;
import frc.command.Command;
import frc.math.DrivingUtility;
import frc.math.DrivingUtility.PathSegment;
import frc.math.Vector;
import frc.subsystem.DrivetrainSubsystem;

public class FollowPathCommand extends Command {
    DrivetrainSubsystem drivetrainSubsystem = ServiceLocator.get(DrivetrainSubsystem.class);
    Vector[] path;

    PathSegment[] pathSegments;
    public FollowPathCommand(PathSegment... pathSegments) {
        this.pathSegments = pathSegments;
    }

    public void init() {
        path = DrivingUtility.makePath(drivetrainSubsystem.getHeading(), drivetrainSubsystem.getRobotPosition(), pathSegments);
        double[] xCoords = new double[path.length];
        double[] yCoords = new double[path.length];
        for(int i = 0 ; i < path.length; i++) {
            xCoords[i] = path[i].x;
            yCoords[i] = path[i].y;
        }
        SmartDashboard.putNumberArray("X coordinates", xCoords);
        SmartDashboard.putNumberArray("Y coordinates", yCoords);
    }

    public void execute() {
        drivetrainSubsystem.purePursuit(path);
    }

    public boolean isFinished() {
        return false;
    }

    public void end() {

    }
}