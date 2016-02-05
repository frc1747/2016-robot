package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

	CANTalon rightClimberTalon, leftClimberTalon;

	public Climber() {
		rightClimberTalon = new CANTalon(RobotMap.RIGHT_CLIMB_CIM);
		leftClimberTalon = new CANTalon(RobotMap.LEFT_CLIMB_CIM);
	}

	public void climbControl(double speed) {
		rightClimberTalon.set(speed);
		leftClimberTalon.set(speed);
	}

	public void climbUp() {
		climbControl(1.0);
	}

	public void goDown() {
		climbControl(-0.5);
	}

	public void initDefaultCommand() {
	}
}
