import static org.junit.Assert.assertEquals;
import org.junit.Test;
import frc.subsystem.DrivetrainSubsystem;
import frc.math.Vector;

public class DrivetrainSubsystemTest {
    @Test
    public void testFindClosestPoint() {
        assertEquals(2, DrivetrainSubsystem.findClosestPoint(new Vector[] {new Vector(0,0), new Vector(1,0), new Vector(2,0),new Vector(3,0)}, new Vector(2,1)));
        
    }

    @Test
    public void testFindGoalPoint() {
        assertEquals(3, DrivetrainSubsystem.findGoalPoint(new Vector[] {new Vector(0,0), new Vector(1,0), new Vector(2,0),new Vector(3,0)}, new Vector(2,1), 10));
        assertEquals(1, DrivetrainSubsystem.findGoalPoint(new Vector[] {new Vector(0,0), new Vector(1,0), new Vector(2,0),new Vector(3,0)}, new Vector(0,1), 1));
        
    }
}