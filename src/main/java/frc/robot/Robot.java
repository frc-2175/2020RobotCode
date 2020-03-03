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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.command.AutoFeedCommand;
import frc.command.Command;
import frc.command.CommandRunner;
import frc.command.ParallelCommand;
import frc.command.RunWhileCommand;
import frc.command.SequentialCommand;
import frc.command.autonomous.AimTurretWithVisionCommand;
import frc.command.autonomous.DriveStraightCommand;
import frc.command.autonomous.FollowPathCommand;
import frc.command.autonomous.IntakeCommand;
import frc.command.autonomous.ShootCommand;
import frc.command.autonomous.TimerCommand;
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
import frc.subsystem.ClimberSubsystem;
import frc.subsystem.ControlPanelSubsystem;
import frc.subsystem.DrivetrainSubsystem;
import frc.subsystem.FeederSubsystem;
import frc.subsystem.IntakeSubsystem;
import frc.subsystem.MagazineSubsystem;
import frc.subsystem.ShooterSubsystem;
import frc.subsystem.ShooterSubsystem.Mode;
import frc.subsystem.VisionSubsystem;

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
  private CommandRunner teleopAutoShootCommand;
  public ClimberSubsystem climberSubsystem;
  public VisionSubsystem visionSubsystem;
  public CommandRunner aimTurretWithVisionCommand; 
  
  
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
  private double initialGyroAngleForVision;
  private double initialTurretOffset;
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

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
    climberSubsystem = new ClimberSubsystem();
    visionSubsystem = new VisionSubsystem();

    SmartDashboard.putNumber("shooter flywheel manual speed", .5);
    

    // Example use of robot logging with SmartDashboard
    logger.log(Logger.INFO, "This is a smart dashboard test!", 
      new LogField("ExampleSmartDashboard", 2175, Logger.SMART_DASHBOARD_TAG));
    propertyDirectory = Filesystem.getDeployDirectory();
    finalThingy = propertyDirectory.getAbsolutePath();

    SequentialCommand crossAutoLineCommand = new SequentialCommand(new Command[] {
      new FollowPathCommand(false, DrivingUtility.makeLinePathSegment(36))
    });

    SequentialCommand crossAutoLineBackwardsCommand = new SequentialCommand(new Command[] {
      new FollowPathCommand(true, DrivingUtility.makeLinePathSegment(36))
    });

    SequentialCommand doNothing = new SequentialCommand();

     /*
    Start on the right side, shoot three balls
    Intake two balls from trench, and shoot
    */
    SequentialCommand rightToTrench = new SequentialCommand(new Command[] {
      //Change all the "123", make robot in line with the trench
      //new AimCommand
      new ShootCommand(4, 4000),
      new ParallelCommand(new Command[] { 
        new FollowPathCommand(false, DrivingUtility.makeLinePathSegment(60.0)), //change later
        new IntakeCommand(5)
      }),
      //new TurretCommand
      //new AimCommand
      new ShootCommand(12, 4000)
    });
    
    /*
    Start in middle, shoot three balls
    Intake three balls from rendezvous, and shoot
    */
    SequentialCommand middleRendezvousThreeBall = new SequentialCommand(new Command[] {
      //Change all the "123", and angle robot towards the three balls
      //new AimCommand
      new ShootCommand(4, 4000),
      new ParallelCommand(new Command[] { 
        //new DriveStraightCommand(123, .5)
      new FollowPathCommand(false, DrivingUtility.makeLinePathSegment(36)), 
      new IntakeCommand(4) //3 balls
      }),
        //new DriveBackwardCommand(123)
      new FollowPathCommand(true, DrivingUtility.makeLinePathSegment(36)), 
      //new AimCommand
      new ShootCommand(9, 4000)
    });

    //drives forward and shoots
    SequentialCommand closeShotAuto = new SequentialCommand(new Command[] {
      new FollowPathCommand(false, DrivingUtility.makeLinePathSegment(115)),
      new RunWhileCommand(new ShootCommand(10, 2900), new AutoFeedCommand())
    });

    SequentialCommand farShotAuto = new SequentialCommand(new Command[] {
      new FollowPathCommand(true, DrivingUtility.makeLinePathSegment(10)),
      new RunWhileCommand(new ShootCommand(10, 3500), new AutoFeedCommand())
    });

    autoChooser.setDefaultOption("Do Nothing", doNothing);
    autoChooser.addOption("Cross Auto Line Forwards", crossAutoLineCommand);
    autoChooser.addOption("Cross Auto Line Backwards", crossAutoLineBackwardsCommand);
    autoChooser.addOption("Far Shot Auto", farShotAuto);
    autoChooser.addOption("Close Shot Auto", closeShotAuto);
    autoChooser.addOption("Right Trench Auto", rightToTrench);
    autoChooser.addOption("Middle Rendezvous Three Ball", middleRendezvousThreeBall);
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
    autonomousCommand = new CommandRunner(autoChooser.getSelected());
  } //29 + 66 / 2 + 252

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    autonomousCommand.runCommand();
    drivetrainSubsystem.periodic();
    shooterSubsystem.periodic();
    climberSubsystem.periodic();
  }

  @Override
  public void teleopInit() {
    teleopAutoShootCommand = new CommandRunner(new AutoFeedCommand());
    aimTurretWithVisionCommand = new CommandRunner(new AimTurretWithVisionCommand());
    shooterSubsystem.updateTurretPIDConstants();
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
    // if(gamepad.getRawButton(GAMEPAD_RIGHT_BUMPER) || leftJoystick.getRawButton(2)) {
    //   magazineSubsystem.magazineRollOut();
    //   intakeSubsystem.intakeRollOut();
    if (gamepad.getRawButton(GAMEPAD_RIGHT_TRIGGER) || rightJoystick.getRawButton(2)) {
      intakeSubsystem.intakeRollIn();
      magazineSubsystem.magazineRollIn();
    } else {
      intakeSubsystem.stopIntake();
      magazineSubsystem.stopMagazine();
    }

    // ✩ intake piston ✩
    if(gamepad.getRawButtonPressed(GAMEPAD_A)) {
      intakeSubsystem.toggleIntake();
    // } else if (gamepad.getRawButtonPressed(GAMEPAD_RIGHT_TRIGGER) || gamepad.getRawButtonPressed(GAMEPAD_RIGHT_BUMPER)) {
    //   intakeSubsystem.putOut();
    } else if (gamepad.getRawButtonReleased(GAMEPAD_RIGHT_TRIGGER) || gamepad.getRawButtonReleased(GAMEPAD_RIGHT_BUMPER)) {
      intakeSubsystem.putIn();
    }



    // ✩ auto shooting command ✩
    if( gamepad.getRawButton(GAMEPAD_X)) {
      // if(gamepad.getRawButtonPressed(GAMEPAD_X)) {
      //   teleopAutoShootCommand.resetCommand();
      // }
      // teleopAutoShootCommand.runCommand();
      feederSubsystem.rollUp();
      magazineSubsystem.magazineRollIn();
    } else {
      teleopAutoShootCommand.endCommand();

      // ✩ feeder roll ✩
      // if(gamepad.getRawButton(GAMEPAD_X)) {
      //   feederSubsystem.rollOutFeeder();
      // } else 
      if (gamepad.getRawButton(GAMEPAD_Y) || gamepad.getRawButton(GAMEPAD_RIGHT_BUMPER)) {
        feederSubsystem.rollUp();
      } else {
        feederSubsystem.stopFeeder();
      }
      
      // ✩ magazine roll ✩ 
      if(rightJoystick.getRawButton(3)) {
        magazineSubsystem.magazineRollIn();
      } else {
        magazineSubsystem.setMagazineMotor(MathUtility.deadband(gamepad.getRawAxis(1), .05) * 0.5); 
        // climberSubsystem.climbSpeed(MathUtility.deadband(gamepad.getRawAxis(1), .05)); 
      }

    }
    



    // ✩ shooter flywheel ✩
    if (gamepad.getRawButton(GAMEPAD_LEFT_TRIGGER)) {
      shooterSubsystem.setTargetSpeed(3000);
      shooterSubsystem.setMode(Mode.BangBang);
    } else if (gamepad.getRawButton(GAMEPAD_LEFT_BUMPER)) {
      shooterSubsystem.setTargetSpeed(SmartDashboard.getNumber("speed goal()rpm??" , 4500));
      shooterSubsystem.setMode(Mode.BangBang);
    } else if (gamepad.getPOV() == POV_DOWN) {
      shooterSubsystem.setMode(Mode.Manual);
      shooterSubsystem.setManualSpeed(SmartDashboard.getNumber("shooter flywheel manual speed", .5)); //manual speed here!!!!!!!
    } else {
      shooterSubsystem.setMode(Mode.Manual);
      shooterSubsystem.setManualSpeed(0);
    }

    // ✩ shooter hood ✩
    if(gamepad.getRawButtonPressed(GAMEPAD_B)) {
      shooterSubsystem.toggleHoodAngle();
    }
    shooterSubsystem.setHoodMotor(MathUtility.deadband(gamepad.getRawAxis(3), .05));

    // ✩ shooter turret ✩
    // if (rightJoystick.getRawButtonPressed(1)) {
    //   shooterSubsystem.setGoalAngle(visionSubsystem.getLimelightHorizontalOffset());
    // } else if (rightJoystick.getRawButton(1)) {
    //   shooterSubsystem.turretPIDToGoalAngle();
    // } else {
    //   shooterSubsystem.setTurretSpeed(0.5 * MathUtility.deadband(Math.pow(gamepad.getRawAxis(2), 2), .05)); // squared inputs babey!!!
    // }

    if (rightJoystick.getRawButton(1)) {
      if (rightJoystick.getRawButtonPressed(1)) {
        aimTurretWithVisionCommand.resetCommand();
      }
      aimTurretWithVisionCommand.runCommand();
    } else if (rightJoystick.getRawButtonReleased(1)) {
      aimTurretWithVisionCommand.endCommand();
    } else {
      shooterSubsystem.setTurretSpeed(0.5 * MathUtility.deadband(Math.pow(gamepad.getRawAxis(2), 2), .05));
    }

    // ✩ climbing subsystem ✩
    if (gamepad.getRawButton(GAMEPAD_START)) {
      climberSubsystem.climbUp();
    } else if (leftJoystick.getRawButton(6)) { 
      climberSubsystem.climbDown();
    } else {
      climberSubsystem.stopClimbing();
    }

    //✩ deploying hook ✩
    if (gamepad.getPOV() == POV_RIGHT) {
      climberSubsystem.deployUp();
    // } else if (gamepad.getPOV() == POV_LEFT) {
    //   climberSubsystem.deployDown();
    } else {
      climberSubsystem.stopDeploy();
    }

    // if(gamepad.getPOV() == POV_UP && gamepad.getPOV() != povLastFrame) {
    //   teleopClimbDeployCommand.resetCommand();
    // } else if(gamepad.getPOV() == POV_UP && gamepad.getPOV() == povLastFrame) {
    //   teleopClimbDeployCommand.runCommand();
    // } else if(gamepad.getPOV() != POV_UP && povLastFrame == POV_UP) {
    //   teleopClimbDeployCommand.endCommand();
    // }
      
    // ✩ Drive Controls ✩
    //drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX() * Math.abs(rightJoystick.getX())); //squared
    // drivetrainSubsystem.blendedDrive(-leftJoystick.getY() , rightJoystick.getX()); //normal
    //drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), (rightJoystick.getX() * Math.abs( rightJoystick.getX() ) )*.5); //squared * .5
    //drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX() * .5); //linear * .5
    if(drivetrainSubsystem.gearsSolenoid.get()) {
      drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX()*.5); //ok actual linear but turning * .5
    } else {
      drivetrainSubsystem.blendedDrive(-leftJoystick.getY(), rightJoystick.getX()); //ok actual linear
    }
    
    

    // ✩ changing gears ✩
    // if (leftJoystick.getRawButtonPressed(3)) { //toggle code
    //   drivetrainSubsystem.toggleGears();
    // }
    drivetrainSubsystem.setGear(leftJoystick.getRawButton(1)); //press and hold code
    
    //✩✩✩you have reached the end of teleop periodic !!!!!!!!!! : ) ✩✩✩
    drivetrainSubsystem.periodic();
    shooterSubsystem.periodic();
    climberSubsystem.periodic();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    drivetrainSubsystem.resetTracking();
    //drivetrainSubsystem.orchestra.loadMusic("careless-whisper.chrp");
    //drivetrainSubsystem.orchestra.play();
  }
}
