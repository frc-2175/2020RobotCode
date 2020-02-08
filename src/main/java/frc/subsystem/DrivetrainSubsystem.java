package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.ServiceLocator;
import frc.VirtualSpeedController;
import frc.info.RobotInfo;

public class DrivetrainSubsystem {
    private final RobotInfo robotInfo;
	private final WPI_TalonFX leftMaster;
    private final BaseMotorController leftFollowerOne;
    private final BaseMotorController leftFollowerTwo;
	private final WPI_TalonFX rightMaster;
    private final BaseMotorController rightFollowerOne;
    private final BaseMotorController rightFollowerTwo;
	private final DifferentialDrive robotDrive;
	public static final double INPUT_THRESHOLD = 0.1; // TODO(low): Constants should move to the top of the class.
	public final AHRS navx;
	double lastEncoderDistanceLeft;
	double lastEncoderDistanceRight;
	private double zeroEncoderLeft;
	private double zeroEncoderRight;
	public static final double TICKS_TO_INCHES = 0.0045933578;

	private static VirtualSpeedController leftVirtualSpeedController = new VirtualSpeedController();
	private static VirtualSpeedController rightVirtualSpeedController = new VirtualSpeedController();
	private static DifferentialDrive virtualRobotDrive;
	
	
    public DrivetrainSubsystem() {
		ServiceLocator.register(this);

		robotInfo = ServiceLocator.get(RobotInfo.class);
		leftMaster = new WPI_TalonFX(1);
        leftFollowerOne = new WPI_VictorSPX(2);
        leftFollowerTwo = new WPI_VictorSPX(3);
		rightMaster = new WPI_TalonFX(4);
        rightFollowerOne = new WPI_VictorSPX(5);
        rightFollowerTwo = new WPI_VictorSPX(6);

        leftFollowerOne.follow(leftMaster);
        leftFollowerTwo.follow(leftMaster);
        rightFollowerOne.follow(rightMaster);
        rightFollowerTwo.follow(rightMaster);

        robotDrive = new DifferentialDrive(leftMaster, rightMaster);
        
        leftVirtualSpeedController = new VirtualSpeedController(); // TODO(low): There is no need to set these here since they are also initialized above.
		rightVirtualSpeedController = new VirtualSpeedController();
		virtualRobotDrive = new DifferentialDrive(leftVirtualSpeedController, rightVirtualSpeedController);

		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

		lastEncoderDistanceLeft = 0;
		lastEncoderDistanceRight = 0;

		navx = new AHRS(SPI.Port.kMXP);
		navx.reset();
    }
         
    public void stopAllMotors() {
           tankDrive(0,0); 
    }
    public static double[] getBlendedMotorValues(double moveValue, double turnValue, double inputThreshold) {
		virtualRobotDrive.arcadeDrive(moveValue, turnValue, false);
		double leftArcadeValue = leftVirtualSpeedController.get() * 0.8;
		double rightArcadeValue = rightVirtualSpeedController.get()* 0.8;

		virtualRobotDrive.curvatureDrive(moveValue, turnValue, false);
		double leftCurvatureValue = leftVirtualSpeedController.get();
		double rightCurvatureValue = rightVirtualSpeedController.get();

		double lerpT = Math.abs(deadband(moveValue, RobotDriveBase.kDefaultDeadband)) / inputThreshold;
		lerpT = clamp(lerpT, 0, 1);
		double leftBlend = lerp(leftArcadeValue, leftCurvatureValue, lerpT);
		double rightBlend = lerp(rightArcadeValue, rightCurvatureValue, lerpT);

		double[] blends = { leftBlend, -rightBlend };
		return blends;
	}

	/**
	 * Drives with a blend between curvature and arcade drive using
	 * linear interpolation
	 *
	 * @param xSpeed the forward/backward speed for the robot
	 * @param zRotation the curvature to drive/the in-place rotation
	 * @see #getBlendedMotorValues(double, double)
	 */
	public void blendedDrive(double xSpeed, double zRotation, double inputThreshold) {
		double[] blendedValues = getBlendedMotorValues(xSpeed, zRotation, inputThreshold);
		robotDrive.tankDrive(blendedValues[0], blendedValues[1]);
	}

	public void blendedDrive(double xSpeed, double zRotation) {
		blendedDrive(xSpeed, zRotation, INPUT_THRESHOLD);
	}

	// TODO(medium): We should move clamp and lerp to a MathUtils class or something. There are now three different subsystems that each have their own definition of clamp, and Robot has its own definition of deadband.

	/**
	 * Clamps a double value based on a minimum and a maximum
	 *
	 * @param val the value to clamp
	 * @param min the minimum to clamp on
	 * @param max the maximum to clamp on
	 * @return min if val is less than min or max if val is greater than max
	 */
	public static double clamp(double val, double min, double max) {
		return val >= min && val <= max ? val : (val < min ? min : max);
	}

	/**
	 * Linearly interpolates between two points based on a t value
	 *
	 * @param a the point to interpolate from
	 * @param b the point to interpolate to
	 * @param t the value to interpolate on
	 * @return an output based on the formula lerp(a, b, t) = (1-t)a + tb
	 */
	public static double lerp(double a, double b, double t) {
		return (1 - t) * a + t * b;
	}

	public static double deadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} else {
				return (value + deadband) / (1.0 - deadband);
			}
		} else {
			return 0.0;
		}
	}

	public static double undeadband(double value, double deadband) {
		if (value < 0) {
			double t = -value;
			return DrivetrainSubsystem.lerp(-deadband, -1, t);
		} else if (value > 0) {
			double t = value;
			return DrivetrainSubsystem.lerp(deadband, 1, t);
		} else {
			return 0;
		}
	}

	public void arcadeDrive(double moveValue, double turnValue) {
		robotDrive.arcadeDrive(-moveValue, -turnValue);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		robotDrive.tankDrive(leftSpeed, rightSpeed);
    }
    public static double proportional(double input, double setpoint, double kp) {
		return (setpoint - input) * kp;
	}
	public double getAverageEncoderDistance() {
		//in inches
		return (((rightMaster.getSelectedSensorPosition(0) + leftMaster.getSelectedSensorPosition(0))/2)*TICKS_TO_INCHES);
	}

	public double getHeading() {
		return navx.getAngle();
	}
	
	public void resetTracking() {
		lastEncoderDistanceLeft = 0;
		lastEncoderDistanceRight = 0;
		zeroEncoderLeft = leftMaster.getSelectedSensorPosition(0);
		zeroEncoderRight = rightMaster.getSelectedSensorPosition(0);
		navx.reset();
	}

}

