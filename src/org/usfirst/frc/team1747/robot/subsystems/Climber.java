package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem implements SDLogger {

	private Talon climberTalon;
	private Talon blowerTalon;
	private Boolean isInflated;

	// maps the left and right climber talons
	public Climber() {
		climberTalon = new Talon(RobotMap.CLIMB_CIM);
		blowerTalon = new Talon(RobotMap.BLOW_CIM);
		isInflated = false;
	}

	// sets up the climber speed for going up
	private void climbControl(double speed) {
		if (isInflated) {
			climberTalon.set(speed);
		}
	}

	// sets up the climber speed for going down
	public void inflationControl(double speed) {// blow up command
		blowerTalon.set(speed);
		isInflated = true;
	}

	public void stopClimb() {
		climbControl(0.0);
	}

	public void stopInflate() {
		inflationControl(0.0);
	}

	public void climbUp() {
		climbControl(1.0);
	}

	public void inflateClimber() {
		inflationControl(1.0);
	}

	public void initDefaultCommand() {
	}

	public void logToSmartDashboard() {

	}
}
