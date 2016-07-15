package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.BasicAuton;
import org.usfirst.frc.team1747.robot.subsystems.Climber;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrainPID;
import org.usfirst.frc.team1747.robot.subsystems.Flashlight;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private static OI oi;
	private static Shooter shooter;
	private static Flashlight flashlight;
	private static DriveTrain drive;
	private static SDController sd;
	private static Intake intake;
	private static Scooper scooper;
	private static Climber climber;
	private static DriveTrainPID pid;
	
	private static final String GAME_STATE = "GameState";
	
	private Command auton;

	public static DriveTrain getDriveTrain() {
		return drive;
	}

	public static OI getOi() {
		return oi;
	}

	public static SDController getSd() {
		return sd;
	}

	public static Shooter getShooter() {
		return shooter;
	}
	
	public static Flashlight getFlashlight() {
		return flashlight;
	}

	public static Intake getIntake() {
		return intake;
	}

	public static Scooper getScooper() {
		return scooper;
	}

	public static DriveTrainPID getDriveTrainPID() {
		return pid;
	}

	public static Climber getClimber() {
		return climber;
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		climber = new Climber();
		shooter = new Shooter();
		flashlight = new Flashlight();
		drive = new DriveTrain();
		intake = new Intake();
		scooper = new Scooper();
		pid = new DriveTrainPID();
		oi = new OI();
		sd = new SDController();
		sd.addSystems(shooter, drive, intake, scooper);
		drive.resetGyro();
    	SmartDashboard.putString(GAME_STATE, "robotInit");
		sd.refresh();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		if(auton != null)
		{
			auton.cancel();
			auton = null;
		}
       	SmartDashboard.putString(GAME_STATE, "disabled");
		sd.refresh();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		sd.refresh();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		(auton = new BasicAuton()).start();
    	SmartDashboard.putString(GAME_STATE, "auton");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		sd.refresh();
	}
	
	@Override
	public void teleopInit() {
		if(auton != null)
		{
			auton.cancel();
			auton = null;
		}
    	SmartDashboard.putString(GAME_STATE, "teleop");
		sd.refresh();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("LEFT SHOOTER SPEED", shooter.getLeftSpeed());
		SmartDashboard.putNumber("RIGHT SHOOTER SPEED", shooter.getRightSpeed());
		sd.refresh();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
		sd.refresh();
	}
}
