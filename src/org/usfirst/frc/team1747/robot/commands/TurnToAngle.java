package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

public class TurnToAngle extends Command {
	private DriveTrain driveTrain;
	private double angle;
	private double time;

	public TurnToAngle() {
		driveTrain = Robot.getDriveTrain();
		// SmartDashboard.putNumber("Turn Angle", Math.PI/2);
		requires(driveTrain);
	}

	protected void initialize() {
		angle = SmartDashboard.getNumber("Turn Angle");
		driveTrain.resetLeftDistance();
		driveTrain.resetRightDistance();
		driveTrain.arcadeDrive(0.0, 0.25);
		time = System.currentTimeMillis();
	}

	protected void execute() {

	}

	protected boolean isFinished() {
		return 0 >= (angle * (16) - (-driveTrain.getRightDistance() + driveTrain.getLeftDistance()) / 2);
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
