package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem implements SDLogger {

	CANTalon rightClimberTalon, leftClimberTalon;
	//maps the left and right climber talons
	public Climber() {
		rightClimberTalon = new CANTalon(RobotMap.RIGHT_CLIMB_CIM);
		leftClimberTalon = new CANTalon(RobotMap.LEFT_CLIMB_CIM);
	}
	//sets the speed of the left and right climber talons
	public void climbControl(double speed) {
		rightClimberTalon.set(speed);
		leftClimberTalon.set(speed);
	}
	//sets up the climber speed for going up
	public void climbUp() {
		climbControl(1.0);
	}
	//sets up the climber speed for going down
	public void goDown() {
		climbControl(-0.5);
	}

	public void initDefaultCommand() {
	}

	@Override
	public void logToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
}
