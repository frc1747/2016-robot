package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.SDLogger;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem implements SDLogger {

	private Talon climberTalon;
	private Talon blowerTalon;

	// maps the left and right climber talons
	public Climber() {
		// climberTalon = new Talon(RobotMap.CLIMB_CIM);
		// blowerTalon = new Talon(RobotMap.BLOW_CIM);
	}

	// sets the speed of the left and right climber talons
	/*
	 * private void climbControl(double speed) { climberTalon.set(speed);
	 * blowerTalon.set(speed); }
	 */
	// sets up the climber speed for going up
	private void climbControl(double speed) {
		climberTalon.set(speed);
	}

	// sets up the climber speed for going down
	public void inflationControl(double speed) {// blow up command
		blowerTalon.set(speed);
	}

	public void stopClimb() {
		climbControl(0.0);
	}

	public void stopInflate() {
		inflationControl(0.0);
	}

	public void climbUp() {
		climbControl(0.5);
	}

	public void inflateClimber() {
		inflationControl(0.5);
	}

	public void initDefaultCommand() {
	}

	public void logToSmartDashboard() {

	}
}
