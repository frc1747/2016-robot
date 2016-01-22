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

	public double getDampeningConstant() {
		return SmartDashboard.getNumber("Dampening Constant");
	}

	public double getElevatorP() {
		return SmartDashboard.getNumber("Elevator P");
	}

	public double getElevatorI() {
		return SmartDashboard.getNumber("Elevator I");
	}

	public double getElevatorD() {
		return SmartDashboard.getNumber("Elevator D");
	}

	public double getRightDamper() {
		return SmartDashboard.getNumber("Right Damper");
	}

	public double getLeftDamper() {
		return SmartDashboard.getNumber("Left Damper");
	}

	public void refresh() {
		oi.getController().logToSmartDashboard();
		driveTrain.logToSmartDashboard();
	}
}