package frc.info;

import java.io.File;
import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.MotorWrapper;
import frc.ServiceLocator;

public class RobotInfo {
	public static interface ValueContainer {
		public Object get();
	}

    private static final String CAN_T_CONTINUE_MSG = "; can't continue";
    private boolean isComp = true;
    private HashMap<String, Object> info;

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
            } else {
                isComp = true;
            } //TODO: add a lil message in case there's nothing there??
        } else { //if the whole directory isn't even there then we guess it's comp??????
            isComp = true;
        }
		ServiceLocator.register(this);
        info = new HashMap<>();
        populate();
    }

    /**
     * This is where all of the information is put into the hash map.
     * @see frc.info.RobotInfo#put(String, Object)
     * */
    public void populate() {
	}

	private MotorWrapper talon(WPI_TalonSRX talon) {
		return new MotorWrapper(talon);
	}

	private MotorWrapper victor(WPI_VictorSPX victor) {
		return new MotorWrapper(victor);
	}

	private MotorWrapper talon(WPI_TalonSRX talon, boolean inverted) {
		return new MotorWrapper(talon, inverted);
	}

	private MotorWrapper victor(WPI_VictorSPX victor, boolean inverted) {
		return new MotorWrapper(victor, inverted);
	}
    /**
     * Puts an object in the hash map
     * @param key the key by which the object is referred to
     * @param comp will be put in the hash map if the robot the code is
     * being run on is the competition robot
     * @param practice will be put in the has map if the robot the code is
     * being run on is the practice robot
     */
    private void put(String key, Object comp, Object practice) {
		Object choice = isComp ? comp : practice;
		info.put(key, choice);
	}
    /**
     * Puts an object in the hash map
     * @param key the key by which the object is referred to
     * @param value the object to put into the hash map (regardless of
     * competition/practice robot)
     */
	private void put(String key, Object value) {
		info.put(key, value);
    }

    /**
     * Puts an object in the hash map. This is used for solenoids to
     * make sure only one solenoid is initialized.
     * <p>Format: put(KEY_VARIABLE, () -> WhateverSolenoidThing(port1, port2),
     * () -> WhateverSolenoidThing(port1, port2))
     * @param key the key by which the object is referred to
     * @param comp
     * @param practice
     */
    private void put(String key, ValueContainer comp, ValueContainer practice) {
		Object choice = isComp ? comp.get() : practice.get();
		info.put(key, choice);
	}

	// TODO(low): Should there be a version of put that take just a single ValueContainer? Technically you could always just use the version that takes a single object, but it might be nice to have consistency.

    /**
     * Gets an object from the hash map
     * @param key the key which refers to the object
     * @param <T> the class of the object. Is usually implicitly set when initializing.
     * @return the object from the hash map
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
		return (T) info.get(key);
	}
}