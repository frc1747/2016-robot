package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDController {

	SendableChooser autonPosition;
	private OI oi;
	private DriveTrain driveTrain;
	private Shooter shooter;
	private Intake intake;
	private Scooper scooper;
	private NetworkTable networkTable = NetworkTable.getTable("imageProcessing");

	public SDController() {
		oi = Robot.getOi();
		driveTrain = Robot.getDriveTrain();
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		SmartDashboard.putData(Scheduler.getInstance());
		autonPosition = new SendableChooser();
		autonPosition.addObject("Don't shoot", 0);
		autonPosition.addObject("1", 1);
		autonPosition.addObject("2", 2);
		autonPosition.addDefault("3", 3);
		autonPosition.addObject("4", 4);
		autonPosition.addObject("5", 5);
		SmartDashboard.putData("Auton Position", autonPosition);
		SmartDashboard.putBoolean("LowerScooperAuto", false);
	}

	public void refresh() {
		// oi.getController().logToSmartDashboard();
		driveTrain.logToSmartDashboard();
		shooter.logToSmartDashboard();
		intake.logToSmartDashboard();
		scooper.logToSmartDashboard();
		SmartDashboard.putString("ShooterDirection", networkTable.getString("ShootDirection", "robotUnknown"));
		SmartDashboard.putNumber("ShooterRads", networkTable.getNumber("ShootRads", 0.0));
	}

	public int getAutonPosition() {
		return (int) autonPosition.getSelected();
	}
}