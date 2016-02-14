package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

public class DriveStraight extends Command {
	DriveTrain driveTrain;

	public DriveStraight() {
		driveTrain = Robot.getDriveTrain();
		System.out.println("Constructor");
		SmartDashboard.putNumber("DriveStraight Distance", 100.0);
		requires(driveTrain);
	}

	@Override
	protected void initialize() {
		System.out.println("Initialize");
		double distance = SmartDashboard.getNumber("DriveStraight Distance", 1000000.0);
		 driveTrain.enablePID();
		 driveTrain.setSetpoint(distance);
	}

	@Override
	protected void execute() {
		 driveTrain.runPID();
	}

	@Override
	protected boolean isFinished() {
		return  driveTrain.isAtTarget();
	}

	@Override
	protected void end() {
		System.out.println("End");
		 driveTrain.disablePID();
		 driveTrain.tankDrive(0.0, 0.0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
