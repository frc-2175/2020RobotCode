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
    DrivetrainSubsystem.PurePursuitResult purePursuitResult; 

    PathSegment[] pathSegments;
    public FollowPathCommand(PathSegment... pathSegments) {
        this.pathSegments = pathSegments;
    }

    public void init() {
        purePursuitResult = null; 
        path = DrivingUtility.makePath(-drivetrainSubsystem.getHeading(), drivetrainSubsystem.getRobotPosition(), pathSegments);
        double[] xCoords = new double[path.length];
        double[] yCoords = new double[path.length];
        for(int i = 0 ; i < path.length; i++) {
            xCoords[i] = path[i].x;
            yCoords[i] = path[i].y;
        }
        SmartDashboard.putNumberArray("Values/PathXCoords", xCoords);
        SmartDashboard.putNumberArray("Values/PathYCoords", yCoords);
    }

    public void execute() {
        purePursuitResult = drivetrainSubsystem.purePursuit(path);
    }

    public boolean isFinished() {
        if (purePursuitResult == null) {
            return false; 
        }
        return purePursuitResult.indexOfClosestPoint == path.length - 1
            && purePursuitResult.goalPoint.y <= 0;
    }

    public void end() {


        drivetrainSubsystem.stopAllMotors();
    }
}