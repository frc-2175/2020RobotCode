package frc.info;

import java.io.File;
import frc.ServiceLocator;

public class RobotInfo {

	public static interface ValueContainer<thing> {
		public thing get();
	}

    private boolean isComp = true;

    public RobotInfo() {
        File propertyDirectory = new File("/home/lvuser");
        if(propertyDirectory.exists()) {
            boolean hasComp = false;
            boolean hasPractice = false;
            File[] lvuserFiles = propertyDirectory.listFiles();
            for (File file : lvuserFiles) { //look through every file in here!! and check what they have
                if ( file.getName().equals("practice")) { //seeing if it has practice file
                    hasPractice = true;
                }
                if (file.getName().equals("comp")) { //seeing if it has comp file
                    hasComp = true; 
                }
            }
            if (!hasComp && hasPractice) { //only be practice mode if there's only the practice file 
                isComp = false;
            } //TODO: add a lil message in case there's nothing there??
        } 
        ServiceLocator.register(this);
    }

    public <thing> thing pick(ValueContainer<thing> comp, ValueContainer<thing> practice) {
        if (isComp) {
            return comp.get();
        } else {
            return practice.get();
        }
    }
}