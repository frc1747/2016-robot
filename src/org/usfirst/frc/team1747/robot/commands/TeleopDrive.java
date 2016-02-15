package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.CyborgController;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleopDrive extends Command {

	DriveTrain driveTrain;
	CyborgController controller;

	public TeleopDrive() {
		driveTrain = Robot.getDriveTrain();
		controller = Robot.getOi().getController();
		requires(driveTrain);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double leftVert = controller.getLeftVert(), rightHoriz = controller.getRightHoriz();
		if (Math.abs(leftVert) < .1 && Math.abs(rightHoriz) < .1) {
			driveTrain.disableRamping();
		} else {
			driveTrain.enableRamping();
		}
		driveTrain.smoothDrive(leftVert, rightHoriz );//* Math.abs(rightHoriz));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
