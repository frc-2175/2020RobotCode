package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ServiceLocator;
import frc.VirtualSpeedController;
import frc.info.RobotInfo;
import frc.math.MathUtility;
import frc.math.Vector; 

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
	private Solenoid gearsSolenoid;
	public static final double TICKS_TO_INCHES = 36/121363.5;
	private Vector position = new Vector(0, 0); 

	private static VirtualSpeedController leftVirtualSpeedController = new VirtualSpeedController();
	private static VirtualSpeedController rightVirtualSpeedController = new VirtualSpeedController();
	private static DifferentialDrive virtualRobotDrive;
	
	
    public DrivetrainSubsystem() {
		ServiceLocator.register(this);

		robotInfo = ServiceLocator.get(RobotInfo.class);
		leftMaster = robotInfo.pick(() -> new WPI_TalonFX(15), () -> new WPI_TalonFX(5));
        leftFollowerOne = robotInfo.pick(() -> new WPI_VictorSPX(11), () -> new WPI_TalonSRX(3));
        leftFollowerTwo = robotInfo.pick(() -> new WPI_VictorSPX(10), () -> new WPI_TalonSRX(4));
		rightMaster = robotInfo.pick(() -> new WPI_TalonFX(16), () -> new WPI_TalonFX(2));
        rightFollowerOne = robotInfo.pick(() -> new WPI_VictorSPX(9), () -> new WPI_TalonSRX(0));
		rightFollowerTwo = robotInfo.pick(() -> new WPI_VictorSPX(8), () -> new WPI_TalonSRX(1));
		gearsSolenoid = new Solenoid(4);

        leftFollowerOne.follow(leftMaster);
        leftFollowerTwo.follow(leftMaster);
        rightFollowerOne.follow(rightMaster);
		rightFollowerTwo.follow(rightMaster);
		
		leftMaster.setInverted(true);
		rightFollowerOne.setInverted(true);
		rightFollowerTwo.setInverted(true);

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
		
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
		leftMaster.setSelectedSensorPosition(0, 0, 0);
		rightMaster.setSelectedSensorPosition(0, 0, 0);
	}
	
	public void periodic() {
		trackLocation();
		System.out.println(position);
		SmartDashboard.putNumber("x", position.x);
		SmartDashboard.putNumber("y", position.y);
		SmartDashboard.putNumber("heading", getHeading());
	}
    
    public void stopAllMotors() {
           tankDrive(0,0); 
	}
	/**
	 * returns motor values from virtual motor controllers !! Don't use this to drive, this helps blendedDrive!!!
	 * 
	 * @param moveValue how much you want to move forward (value from -1 to 1)
	 * @param turnValue how much you want to turn (value from -1 to 1)
	 * @param inputThreshold built-in deadband
	 * @return
	 */
    public static double[] getBlendedMotorValues(double moveValue, double turnValue, double inputThreshold) {
		virtualRobotDrive.arcadeDrive(moveValue, turnValue, false);
		double leftArcadeValue = leftVirtualSpeedController.get() * 0.8;
		double rightArcadeValue = rightVirtualSpeedController.get()* 0.8;

		virtualRobotDrive.curvatureDrive(moveValue, turnValue, false);
		double leftCurvatureValue = leftVirtualSpeedController.get();
		double rightCurvatureValue = rightVirtualSpeedController.get();

		double lerpT = Math.abs(MathUtility.deadband(moveValue, RobotDriveBase.kDefaultDeadband)) / inputThreshold;
		lerpT = MathUtility.clamp(lerpT, 0, 1);
		double leftBlend = MathUtility.lerp(leftArcadeValue, leftCurvatureValue, lerpT);
		double rightBlend = MathUtility.lerp(rightArcadeValue, rightCurvatureValue, lerpT);

		double[] blends = { leftBlend, rightBlend };
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

	/**
	 * Drives with a blend between curvature and arcade drive using
	 * linear interpolation (this version doesn't use an input threshold)
	 * 
	 * @param xSpeed the forward/backward speed for the robot
	 * @param zRotation the curvature to drive/the in-place rotation
	 */
	public void blendedDrive(double xSpeed, double zRotation) {
		blendedDrive(xSpeed, zRotation, INPUT_THRESHOLD);
	}

	// TODO(medium): We should move clamp and lerp to a MathUtils class or something. (done) There are now three different subsystems that each have their own definition of clamp, and Robot has its own definition of deadband.
	/**
	 * reverses a deadbanded value
	 * 
	 * @param value value that has been deadbanded 
	 * @param deadband size of deadband
	 * @return undeadbanded value
	 */
	public static double undeadband(double value, double deadband) {
		if (value < 0) {
			double t = -value;
			return MathUtility.lerp(-deadband, -1, t);
		} else if (value > 0) {
			double t = value;
			return MathUtility.lerp(deadband, 1, t);
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
	
	/**
	 * resets encoder distances & gyro 
	 */
	public void resetTracking() {
		lastEncoderDistanceLeft = 0;
		lastEncoderDistanceRight = 0;
		zeroEncoderLeft = leftMaster.getSelectedSensorPosition(0);
		zeroEncoderRight = rightMaster.getSelectedSensorPosition(0);
		navx.reset();
	}

	/**
	 * changes gears for drive train
	 */
	public void toggleGears() {
		gearsSolenoid.set(!gearsSolenoid.get());
	}

	public void setLowGear() {
		gearsSolenoid.set(true);
	}

	public void setHighGear() {
		gearsSolenoid.set(false);
	}

	public void setGear(boolean gear) {
		gearsSolenoid.set(gear);
	}

	public double getLeftDistance() {
		SmartDashboard.putNumber("leftEncoderDistance", leftMaster.getSelectedSensorPosition());
		SmartDashboard.putNumber("leftDistanceInches", leftMaster.getSelectedSensorPosition() * TICKS_TO_INCHES );
		return leftMaster.getSelectedSensorPosition() * TICKS_TO_INCHES;
	}
	public double getRightDistance() {
		SmartDashboard.putNumber("rightEncoderDistance", rightMaster.getSelectedSensorPosition());
		SmartDashboard.putNumber("rightDistanceInches", rightMaster.getSelectedSensorPosition() * TICKS_TO_INCHES );
		return rightMaster.getSelectedSensorPosition() * TICKS_TO_INCHES;
	}

	public Vector getRobotPosition() {
		return position; 
	}

	public void trackLocation() {
		double distanceLeft = getLeftDistance() - lastEncoderDistanceLeft; 
		double distanceRight = getRightDistance() - lastEncoderDistanceRight; 
		double distance = (distanceLeft + distanceRight) / 2; 
		double angle = Math.toRadians(navx.getAngle()); 

		double x = Math.sin(angle) * distance; 
		double y = Math.cos(angle) * distance; 

		Vector changeInPosition = new Vector(x, y); 
		position = position.add(changeInPosition); 

		lastEncoderDistanceLeft = getLeftDistance(); 
		lastEncoderDistanceRight = getRightDistance();
	}


}

