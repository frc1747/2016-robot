package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public class BallEject extends Command {
	
	Intake intake;
	DigitalInput liftInput, intakeInput;
	double startTime;
	
	//input senses if the arm is low enough to get the ball
	//input2 senses if we have a ball
	
	public BallEject(){
		intake = Robot.getIntake();
		liftInput = intake.getLiftInput();
		intakeInput = intake.getIntakeInput();
		requires(intake);
	}
	
	//Put the arm in position to eject the ball it is holding
	protected void initialize() {
		intake.liftControl(0.5);
		if(liftInput.get()){
			intake.liftControl(0);
		}
		startTime = System.currentTimeMillis(); 
		
	}
	
	//Eject the ball
	protected void execute() {
		intake.rollerControl(-0.5);
	}

	//After 2000milliseconds, stop the motors
	protected boolean isFinished() {
		return (System.currentTimeMillis() - startTime >= 2000);
	}

	
	protected void end() {
		intake.rollerControl(0.0);
	}

	@Override
	protected void interrupted() {
	}

}
