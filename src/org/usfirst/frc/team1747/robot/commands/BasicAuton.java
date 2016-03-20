package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BasicAuton extends CommandGroup {
	// auto mode uses DriveStraight and AutoShoot
	public BasicAuton() {
		if (SmartDashboard.getBoolean("LowerScooperAuto")) {
			addSequential(new LowerScooper());
		}
		addSequential(new DriveStraight());
		addSequential(new RaiseLift());
		// addSequential(new LowerScooper());
		addSequential(new AutoShoot());
	}
}
