package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDController {

	private OI oi;
	private DriveTrain driveTrain;

	public SDController() {
		oi = Robot.getOi();
		driveTrain = Robot.getDrive();
		SmartDashboard.putData(Scheduler.getInstance());
	}

	public void refresh() {
		oi.getController().logToSmartDashboard();
		driveTrain.logToSmartDashboard();
	}
}