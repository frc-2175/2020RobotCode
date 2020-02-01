package command.autonomous;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import frc.command.autonomous.SpinControlPanelCommand;

public class SpinControlPanelCommandTest {
   @Test
   public void testGetShortestDistance(){
        assertEquals(-45, SpinControlPanelCommand.getShortestDistance("red", "green"), 0.001);
        assertEquals(0, SpinControlPanelCommand.getShortestDistance("green", "yellow"), 0.001);
        assertEquals(-45, SpinControlPanelCommand.getShortestDistance("green", "blue"), 0.001);
        assertEquals(90, SpinControlPanelCommand.getShortestDistance("green", "green"), 0.001);
        assertEquals(45, SpinControlPanelCommand.getShortestDistance("yellow", "blue"), 0.001);
   }
}