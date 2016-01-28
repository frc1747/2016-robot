package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDController {

	private OI oi;
	private DriveTrain driveTrain;
	private Shooter shooter;

	public SDController() {
		oi = Robot.getOi();
		driveTrain = Robot.getDrive();
		shooter=Robot.getShooter();
		SmartDashboard.putData(Scheduler.getInstance());
	}

	public void refresh() {
		oi.getController().logToSmartDashboard();
		driveTrain.logToSmartDashboard();
		shooter.logToSmartDashboard();
	}
}