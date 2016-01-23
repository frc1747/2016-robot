package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class BallEject extends Command {
	
	Intake intake;
	DigitalInput input, input2;
	double startTime;
	
	//input senses if the arm is low enough to get the ball
	//input2 senses if we have a ball
	
	public BallEject(){
		intake = Robot.getIntake();
		input = intake.getLiftInput();
		input2 = intake.getIntakeInput();
		requires(intake);
	}

	protected void initialize() {
		intake.liftControl(0.5);
		if(input.get()){
			intake.liftControl(0);
		}
		startTime = System.currentTimeMillis(); 
		
	}

	protected void execute() {
		intake.rollerControl(-0.5);
	}

	@Override
	protected boolean isFinished() {
		if(System.currentTimeMillis() - startTime >= 2000){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	protected void end() {
		intake.rollerControl(0);
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
