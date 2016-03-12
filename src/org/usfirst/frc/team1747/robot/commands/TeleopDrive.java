package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Cyborg;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TeleopDrive extends Command {

	DriveTrain driveTrain;
	Cyborg controller;
	Cyborg auxController;
	double turnDampening;
	SmartDashboard smartDashboard;

	public TeleopDrive() {
		driveTrain = Robot.getDriveTrain();
		controller = Robot.getOi().getController();
		auxController = Robot.getOi().getAuxController();
		requires(driveTrain);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		turnDampening = driveTrain.getTeleopTurnDampener();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double leftVert = controller.getLeftVert(), rightHoriz = controller.getRightHoriz();
		if (rightHoriz < 0) {
			rightHoriz *= 1.1;// Add to compensate for turning left, may not
								// work
		}
		if (Math.abs(leftVert) < .1 && Math.abs(rightHoriz) < .1) {
			driveTrain.disableRamping();
		} else {
			driveTrain.enableRamping();
		}

		driveTrain.smoothDrive(leftVert, rightHoriz * (auxController.getRightBumper().get() ? turnDampening : 1));// *
																													// Math.abs(rightHoriz));
		// driveTrain.arcadeDrive(leftVert, rightHoriz);
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
