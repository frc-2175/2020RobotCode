import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.closeTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import frc.math.DrivingUtility;

public class DrivingUtilityTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void testGetTrapezoidSpeed(){
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, -1), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 0), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 0.5), closeTo(0.5, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 1), closeTo(0.8, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 2), closeTo(0.8, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 3), closeTo(0.8, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 4), closeTo(0.6, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 5), closeTo(0.4, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 5, 1, 2, 6), closeTo(0.4, 0.01));

        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, -1), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 0), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 0.5), closeTo(0.5, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 1), closeTo(0.8, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 2), closeTo(0.6, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 3), closeTo(0.4, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 1, 2, 4), closeTo(0.4, 0.01));

        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 2, 2, -1), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 2, 2, 0), closeTo(0.2, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 2, 2, 1.5), closeTo(0.3, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 2, 2, 3), closeTo(0.4, 0.01));
        collector.checkThat(DrivingUtility.getTrapezoidSpeed(0.2, 0.8, 0.4, 3, 2, 2, 4), closeTo(0.4, 0.01));
    }

}