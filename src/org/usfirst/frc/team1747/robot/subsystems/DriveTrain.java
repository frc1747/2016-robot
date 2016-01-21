package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	CANTalon leftTalon;
	CANTalon rightTalon;
	CANTalon leftTalon2;
	CANTalon rightTalon2;

	public DriveTrain() {
		leftTalon = new CANTalon(1);
		rightTalon = new CANTalon(2);
		leftTalon2 = new CANTalon(22);
		rightTalon2 = new CANTalon(11);
		
	}
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void TankDrive(double leftSpeed, double rightSpeed){
		leftTalon.set(-leftSpeed);
		rightTalon.set(rightSpeed);
		leftTalon2.set(-leftSpeed);
		rightTalon2.set(rightSpeed);
		
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new TeleopDrive());
		
	}
}
