package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BasicAuton extends CommandGroup {
    
    public  BasicAuton() {
    	addSequential(new DriveStraight());
    	addSequential(new AutoShoot());
    }
}
