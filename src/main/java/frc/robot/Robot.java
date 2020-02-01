/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.info.RobotInfo;
import frc.logging.LogHandler;
import frc.logging.LogServer;
import frc.logging.Logger;
import frc.logging.StdoutHandler;
import frc.subsystem.ControlPanelSubsystem;
import frc.subsystem.DrivetrainSubsystem;
import frc.subsystem.IntakeSubsystem;
import frc.subsystem.ShooterSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Logger logger = new Logger(new LogHandler[] {
    new StdoutHandler()
  });

  WPI_VictorSPX leftMotor1;
  WPI_VictorSPX leftMotor2;
  WPI_TalonSRX rightMotor1;
  WPI_VictorSPX rightMotor2;
  Joystick gamepad;
  Joystick leftJoystick;
  Joystick rightJoystick;
  public RobotInfo robotInfo;
  public IntakeSubsystem intakeSubsystem;
  public ShooterSubsystem shooterSubsystem;
  public ControlPanelSubsystem controlPanelSubsystem;
  public DrivetrainSubsystem drivetrainSubsystem;
  

  /*
      (y)
  (x)     (b)
      (a)
  */
  public static final int GAMEPAD_X = 1;
	public static final int GAMEPAD_A = 2;
	public static final int GAMEPAD_B = 3;
	public static final int GAMEPAD_Y = 4;
	public static final int GAMEPAD_LEFT_BUMPER = 5;
	public static final int GAMEPAD_RIGHT_BUMPER = 6;
	public static final int GAMEPAD_LEFT_TRIGGER = 7;
	public static final int GAMEPAD_RIGHT_TRIGGER = 8;
	public static final int GAMEPAD_BACK = 9;
	public static final int GAMEPAD_START = 10;
	public static final int GAMEPAD_LEFT_STICK_PRESS = 11;
	public static final int GAMEPAD_RIGHT_STICK_PRESS = 12;

	public static final int POV_UP = 0;
	public static final int POV_UP_RIGHT = 45;
	public static final int POV_RIGHT = 90;
	public static final int POV_DOWN_RIGHT = 135;
	public static final int POV_DOWN = 180;
	public static final int POV_DOWN_LEFT = 225;
	public static final int POV_LEFT = 270;
  public static final int POV_UP_LEFT = 315;

  public static final double topSpeed = 1;
  
  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    leftMotor2 = new WPI_VictorSPX(2);
    leftMotor1 = new WPI_VictorSPX(5);
    rightMotor1 = new WPI_TalonSRX(9);
    rightMotor2 = new WPI_VictorSPX(1);
    gamepad = new Joystick(0);
    leftJoystick = new Joystick(1);
    rightJoystick = new Joystick(2);
    robotInfo = new RobotInfo();
    intakeSubsystem = new IntakeSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    controlPanelSubsystem = new ControlPanelSubsystem();
    LogServer logServer = new LogServer();
    logServer.startServer();
    drivetrainSubsystem = new DrivetrainSubsystem();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
    logger.info("Robot program is disabled and ready.");
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    leftMotor2.set(deadband(-gamepad.getRawAxis(1),.05)*topSpeed); //set left side wheels to go by gamepad joystick, and modify by top speed ratio
    rightMotor2.set(deadband(-gamepad.getRawAxis(3),.05)*topSpeed);
    if(gamepad.getRawButton(GAMEPAD_LEFT_BUMPER)) {
      leftMotor1.set(-.7);
      rightMotor1.set(.7);
    } else {
      leftMotor1.set(0);
      rightMotor1.set(0);
    }
    if(gamepad.getRawButton(GAMEPAD_B)) {
      leftMotor2.set(-.75*topSpeed);
      rightMotor2.set(1*topSpeed);
    } else if(gamepad.getRawButton(GAMEPAD_A)) {
      leftMotor2.set(-.5*topSpeed);
      rightMotor2.set(1*topSpeed);
    }
    if(gamepad.getRawButton(GAMEPAD_RIGHT_BUMPER)) {
      intakeSubsystem.intakeRollIn();
    } else if (gamepad.getRawButton(GAMEPAD_RIGHT_TRIGGER)) {
      intakeSubsystem.intakeRollOut();
    } else {
      intakeSubsystem.stopIntake();
    }
    if (gamepad.getRawButton(GAMEPAD_A)) {
      shooterSubsystem.shootOut();
    } else {
      shooterSubsystem.stopShootOut();
    }
    if(gamepad.getRawButton(GAMEPAD_X)) {
      controlPanelSubsystem.spinControlPanelForward();
    } else {
      controlPanelSubsystem.stopSpinControlPanel();
    }
    //driving stuff I guess.....
    drivetrainSubsystem.blendedDrive(leftJoystick.getY(), rightJoystick.getX());
    //end of teleop periodic !!!!!!!!!!
    double hue = ControlPanelSubsystem.getHue(controlPanelSubsystem.getColorSensorRed(), 
      controlPanelSubsystem.getColorSensorGreen(), controlPanelSubsystem.getColorSensorBlue());
    SmartDashboard.putNumber("ColorSensorRed", controlPanelSubsystem.getColorSensorRed());
    SmartDashboard.putNumber("ColorSensorGreen", controlPanelSubsystem.getColorSensorGreen());
    SmartDashboard.putNumber("ColorSensorBlue", controlPanelSubsystem.getColorSensorBlue());
    SmartDashboard.putString("ControlPanelColor", ControlPanelSubsystem.getControlPanelColor(hue));

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
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
}
