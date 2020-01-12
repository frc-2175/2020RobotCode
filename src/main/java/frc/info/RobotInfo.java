package frc.info;

import java.io.File;
import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.MotorWrapper;
import frc.ServiceLocator;
import frc.SolenoidWrapper;
import frc.info.RobotInfo.ValueContainer;

public class RobotInfo {

    public static final String LEFT_MOTOR_MASTER = "LEFT_MOTOR_MASTER";
    public static final String LEFT_MOTOR_FOLLOWER_ONE = "LEFT_MOTOR_FOLLOWER_ONE";
    public static final String LEFT_MOTOR_FOLLOWER_TWO = "LEFT_MOTOR_FOLLOWER_TWO";
	public static final String RIGHT_MOTOR_MASTER = "RIGHT_MOTOR_MASTER";
    public static final String RIGHT_MOTOR_FOLLOWER_ONE = "RIGHT_MOTOR_FOLLOWER_ONE";
    public static final String RIGHT_MOTOR_FOLLOWER_TWO = "RIGHT_MOTOR_FOLLOWER_TWO";
    public static final String LEFT_PISTON = "LEFT_PISTON";
    public static final String RIGHT_PISTON = "RIGHT_PISTON";
    public static final String INTAKE_MOTOR = "INTAKE_MOTOR";
    
	public static interface ValueContainer {
		public Object get();
	}

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
        put(LEFT_MOTOR_MASTER, () -> talon(new WPI_TalonSRX(1)));
        put(LEFT_MOTOR_FOLLOWER_ONE, () -> victor(new WPI_VictorSPX(2)));
        put(LEFT_MOTOR_FOLLOWER_TWO, () -> victor(new WPI_VictorSPX(3)));
        put(RIGHT_MOTOR_MASTER, () -> talon(new WPI_TalonSRX(4)));
		put(RIGHT_MOTOR_FOLLOWER_ONE, () -> victor(new WPI_VictorSPX(5)));
        put(RIGHT_MOTOR_FOLLOWER_TWO, () -> victor(new WPI_VictorSPX(6)));
        put(LEFT_PISTON, () -> new SolenoidWrapper(1));
        put(RIGHT_PISTON, () -> new SolenoidWrapper(2));
        put(INTAKE_MOTOR, () -> talon(new WPI_TalonSRX(9)));
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
    
    private void put(String key, ValueContainer value) {
        info.put(key, value.get());
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