package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BasicAuton extends CommandGroup {
	// auto mode uses DriveStraight and AutoShoot
	public BasicAuton() {
		if (SmartDashboard.getBoolean("LowerScooperAuto", false)) {
			addParallel(new LowerLift());
			addSequential(new LowerScooper());
		}
		addSequential(new DriveStraightForward());
		addSequential(new RaiseLift());
		addSequential(new AutoShoot());
		if (SmartDashboard.getBoolean("BackUpInAuto", false)) {
			addParallel(new RaiseScooper());
			addSequential(new RaiseLift());
			addSequential(new DriveStraightBack());
		}
	}
}