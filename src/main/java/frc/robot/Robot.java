/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.command.Command;
import frc.command.CommandRunner;
import frc.command.ParallelCommand;
import frc.command.SequentialCommand;
import frc.command.autonomous.DriveBackwardCommand;
import frc.command.autonomous.DriveStraightCommand;
import frc.command.autonomous.FollowPathCommand;
import frc.command.autonomous.IntakeCommand;
import frc.command.autonomous.ShootCommand;
import frc.command.autonomous.TurningDegreesCommand;
import frc.info.RobotInfo;
import frc.logging.JSONHandler;
import frc.logging.LogField;
import frc.logging.LogHandler;
import frc.logging.LogServer;
import frc.logging.Logger;
import frc.logging.SmartDashboardHandler;
import frc.logging.StdoutHandler;
import frc.math.DrivingUtility;
import frc.math.MathUtility;
import frc.subsystem.ControlPanelSubsystem;
import frc.subsystem.DrivetrainSubsystem;
import frc.subsystem.FeederSubsystem;
import frc.subsystem.IntakeSubsystem;
import frc.subsystem.MagazineSubsystem;
import frc.subsystem.ShooterSubsystem;
import frc.subsystem.ClimberSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Logger logger = new Logger(new LogHandler[] {
    new StdoutHandler(),
    new JSONHandler("/home/lvusr/logs/test.log"),
    new SmartDashboardHandler()
  }, true);

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
  public FeederSubsystem feederSubsystem;
  public MagazineSubsystem magazineSubsystem;
  private CommandRunner autonomousCommand;
  public ClimberSubsystem climberSubsystem;
  
  
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
  File propertyDirectory;
  String finalThingy;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    gamepad = new Joystick(2);
    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
    robotInfo = new RobotInfo();
    intakeSubsystem = new IntakeSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    controlPanelSubsystem = new ControlPanelSubsystem();
    LogServer logServer = new LogServer();
    logServer.startServer();
    drivetrainSubsystem = new DrivetrainSubsystem();
    feederSubsystem = new FeederSubsystem();
    magazineSubsystem = new MagazineSubsystem();
    

    // Example use of robot logging with SmartDashboard
    logger.log(Logger.INFO, "This is a smart dashboard test!", 
      new LogField("ExampleSmartDashboard", 2175, Logger.SMART_DASHBOARD_TAG));
      propertyDirectory = Filesystem.getDeployDirectory();
      finalThingy = propertyDirectory.getAbsolutePath();
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
    SequentialCommand crossAutoLineCommand = new SequentialCommand(new Command[] {
      new DriveStraightCommand(4, .6)
    });

     /*
    Start on the right side, shoot three balls
    Intake two balls from trench, and shoot
    */
    SequentialCommand rightToTrench = new SequentialCommand(new Command[] {
      //Change all the "123", make robot in line with the trench
      //new AimCommand
      new ShootCommand(123),
      new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5), //change later
        new IntakeCommand(123)
      }),
      new TurningDegreesCommand(-180), //could make a turn shooter command
      //new AimCommand
      new ShootCommand(123) 
    });

    /*
    Start on the right side, shoot three balls
    Intake five balls from rendezvous, and shoot
    */
    SequentialCommand rightToRendezvous = new SequentialCommand(new Command[] {
      //Change all the "123", make robot face rendezvous area before match
      //new AimCommand
      new ShootCommand(123),
      new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5), //change later
        new IntakeCommand(123)
      }),
      new TurningDegreesCommand(123),
      new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5), //change later
        new IntakeCommand(123)
      }),
      //new AimCommand
      new ShootCommand(123)
    });
    
    /*
    Start in middle, shoot three balls
    Intake three balls from rendezvous, and shoot
    */
    SequentialCommand middleRendezvousThreeBall = new SequentialCommand(new Command[] {
      //Change all the "123", and angle robot towards the three balls
      //new AimCommand
      new ShootCommand(123),
      new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5), //change later
        new IntakeCommand(123) //3 balls
      }),
      new DriveBackwardCommand(123),
      //new AimCommand
      new ShootCommand(123)
    });

    /*
    Start in middle, shoot three balls
    Intake all five balls from rendezvous point, and shoot
    */
    SequentialCommand middleRendezvousFiveBall = new SequentialCommand(new Command[] {
      //Change all the "123", and angle robot towards the three balls
      //new AimCommand
      new ShootCommand(123),
      new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5), //change later
        new IntakeCommand(123) //5 balls
      }),
      //new AimCommand
      new ShootCommand(123)
    });

    /*
    Start on left side w/ three balls
    Drive into opp. side trench, intake two balls
    Drive out of trench into middle area, and shoot
    */
    SequentialCommand leftTrench = new SequentialCommand(new Command[] {
       //face two balls in oposite trench
       //change 123
       new ParallelCommand(new Command[] { 
        new DriveStraightCommand(123, .5),
        new IntakeCommand(123)
      }),
      new DriveBackwardCommand(123), //out of trench
      new TurningDegreesCommand(123),
      new DriveStraightCommand(123, .5),
      //new AimCommand
      new ShootCommand(123)
    });

    FollowPathCommand purePursuit = new FollowPathCommand( false, 
      DrivingUtility.makeLinePathSegment(29), //2 feet ish
      DrivingUtility.makeLeftArcPathSegment(33, 90), //33 r
      DrivingUtility.makeRightArcPathSegment(33, 90), //33 r
      DrivingUtility.makeLinePathSegment(222) //252
    );
    FollowPathCommand purePursuit2 = new FollowPathCommand( true, 
    DrivingUtility.makeLinePathSegment(29), //2 feet ish
    DrivingUtility.makeLeftArcPathSegment(33, 90), //33 r
    DrivingUtility.makeRightArcPathSegment(33, 90), //33 r
    DrivingUtility.makeLinePathSegment(20) //252
    );

    autonomousCommand = new CommandRunner(purePursuit2); //INSERT COMMAND TO RUN HERE!!!!!!!!!!!!!!!!!
  } //29 + 66 / 2 + 252

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    autonomousCommand.runCommand();
    drivetrainSubsystem.periodic();
    shooterSubsystem.periodic();
  }

  /**
   * This function is called periodically during operator control.
   */
  /*
  ✩ controls (weapons) ✩
    ✩ face buttons ✩
      -X : unfeed ball from feeder
      -Y : feed ball into shooter (with feeder)
      -A : Toggle intake out and in (one double solenoid)
      -B : Hood toggle (one double solenoid)
    ✩ Bumpers / triggers ✩ 
      -Left bumper : none
      -Left trigger : spin flywheel
      -Right bumper : intake spin out
      -Right trigger : intake spin in
    ✩ Sticks (on gamepad) ✩
      -left stick Y : Magazine roll out/in
      -Right stick X : Manual move turret (side to side)
      -Right stick Y : Manual move turret angle up/down
    ✩ Climb controls ✩
      -Start and Back
    ✩ D - Pad ✩
      -nothing lol
  */
  @Override
  public void teleopPeriodic() {
    // ✩ intake roll ✩
    if(gamepad.getRawButton(GAMEPAD_RIGHT_BUMPER)) {
      intakeSubsystem.intakeRollOut();
    } else if (gamepad.getRawButton(GAMEPAD_RIGHT_TRIGGER)) {
      intakeSubsystem.intakeRollIn();
    } else {
      intakeSubsystem.stopIntake();
    }

    // ✩ intake piston ✩
    if(gamepad.getRawButtonPressed(GAMEPAD_A)) {
      intakeSubsystem.toggleIntake();
    } else if (gamepad.getRawButtonPressed(GAMEPAD_RIGHT_TRIGGER) || gamepad.getRawButtonPressed(GAMEPAD_RIGHT_BUMPER)) {
      intakeSubsystem.putOut();
    } else if (gamepad.getRawButtonReleased(GAMEPAD_RIGHT_TRIGGER) || gamepad.getRawButtonReleased(GAMEPAD_RIGHT_BUMPER)) {
      intakeSubsystem.putIn();
    }

    // ✩ feeder roll ✩
    if(gamepad.getRawButton(GAMEPAD_X)) {
      feederSubsystem.rollInFeeder();
    } else if (gamepad.getRawButton(GAMEPAD_Y)) {
      feederSubsystem.rollOutFeeder();
    } else {
      feederSubsystem.stopFeeder();
    }

    // ✩ magazine roll ✩ 
    magazineSubsystem.setMagazineMotor(MathUtility.deadband(gamepad.getRawAxis(1), .05));

    // ✩ shooter flywheel ✩
    if (gamepad.getRawButton(GAMEPAD_LEFT_TRIGGER)) {
      //shooterSubsystem.shootOut();
    } else {
      //shooterSubsystem.stopShootOut();
    }

    //climbing subsystem
    if (gamepad.getRawButton(GAMEPAD_START)) {
      climberSubsystem.climbUp();
    } else if (gamepad.getRawButton(GAMEPAD_BACK)) { 
      climberSubsystem.climbDown();
    } else {
      climberSubsystem.stopClimbing();
    }

    //deploying hook
    if (gamepad.getPOV() == POV_UP) {
      climberSubsystem.deployUp();
    } else if (gamepad.getPOV() == POV_DOWN) {
      climberSubsystem.deployDown();
    } else {
      climberSubsystem.stopDeploy();
    }
      

    // ✩ Drive Controls ✩
    //drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX() * Math.abs(rightJoystick.getX())); //squared
    // drivetrainSubsystem.blendedDrive(-leftJoystick.getY() , rightJoystick.getX()); //normal
    //drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), (rightJoystick.getX() * Math.abs( rightJoystick.getX() ) )*.5); //squared * .5
    drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX() * .5); //linear * .5

    // ✩ changing gears ✩
    // if (leftJoystick.getRawButtonPressed(3)) { //toggle code
    //   drivetrainSubsystem.toggleGears();
    // }
    drivetrainSubsystem.setGear(leftJoystick.getRawButton(1)); //press and hold code
    
    //you have reached the end of teleop periodic !!!!!!!!!! : )
    drivetrainSubsystem.periodic();
    shooterSubsystem.periodic();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    drivetrainSubsystem.resetTracking();
    drivetrainSubsystem.orchestra.loadMusic("careless-whisper.chrp");
    drivetrainSubsystem.orchestra.play();
  }
}
