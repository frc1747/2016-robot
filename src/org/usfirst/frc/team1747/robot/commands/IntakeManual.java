package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeManual extends Command{
	
	Intake intake;
	double speed;

	
	
	//input senses if the arm is low enough to get the ball
	//input2 senses if we have a ball
	
	public IntakeManual(double speed){
		intake = Robot.getIntake();
		requires(intake);
		this.speed = speed;
	}

	protected void initialize() {
		intake.rollerControl(speed);
	}

	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return false;
		// TODO Auto-generated method stub
		//System.out.println(System.currentTimeMillis() - startTime);
		//System.currentTimeMillis() - startTime > 50000.0;
	}

	@Override
	protected void end() {
		
		}
		// TODO Auto-generated method stub
		//shooter.shoot(0.0);

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
	

}