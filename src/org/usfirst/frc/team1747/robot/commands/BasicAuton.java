package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class BasicAuton extends CommandGroup {
	// auto mode uses DriveStraight and AutoShoot
	public BasicAuton() {
		addSequential(new DriveStraight());
		addSequential(new RaiseLift());
		addSequential(new AutoShoot());
	}
}
