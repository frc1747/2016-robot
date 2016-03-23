package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.GyroITG3200;

import edu.wpi.first.wpilibj.command.Command;

public class ResetGyro extends Command {

	GyroITG3200 gyro;

	public ResetGyro() {
		gyro = Robot.getGyro();

	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		gyro.reset();
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
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
