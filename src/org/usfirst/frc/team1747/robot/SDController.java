package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDController {

	private OI oi;
	private DriveTrain driveTrain;
	private Shooter shooter;
	private Intake intake;
	private NetworkTable networkTable = NetworkTable.getTable("imageProcessing");

	public SDController() {
		oi = Robot.getOi();
		driveTrain = Robot.getDrive();
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		SmartDashboard.putData(Scheduler.getInstance());
	}

	public void refresh() {
		//oi.getController().logToSmartDashboard();
		driveTrain.logToSmartDashboard();
		shooter.logToSmartDashboard();
		intake.logToSmartDashboard();
		SmartDashboard.putString("ShooterDirection", networkTable.getString("ShootDirection", "robotUnknown"));
	}
}