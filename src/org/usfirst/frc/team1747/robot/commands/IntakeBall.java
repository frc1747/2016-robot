package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeBall extends Command {
	
	Intake intake;
	DigitalInput input = new DigitalInput(1);
	
	public IntakeBall(){
		intake = Robot.getIntake();
		requires(intake);
	}

	protected void initialize() {
		intake.liftControl(0.5);
		if(input.get()){
			intake.liftControl(0);
		}
	}

	protected void execute() {
		intake.rollerControl(0.5);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		//System.out.println(System.currentTimeMillis() - startTime);
		return false;//System.currentTimeMillis() - startTime > 50000.0;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		//shooter.shoot(0.0);
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
	

}