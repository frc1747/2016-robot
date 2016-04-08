package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;

public class BasicAuton extends CommandGroup {
    // auto mode uses DriveStraight and AutoShoot
    SDController sd;
    SDController.Positions pos;
    SDController.Defense def;

    public BasicAuton() {
        sd = Robot.getSd();
        pos = sd.getAutonPosition();
        def = sd.getDefenseType();

        if (SmartDashboard.getBoolean("LowerScooperAuto", false)) {
            addParallel(new LowerLift());
            addSequential(new LowerScooper());
        }
        addSequential(new DriveStraightForward());
        addSequential(new RaiseLift());
        addSequential(new AutoShoot());
        if (SmartDashboard.getBoolean("BackUpInAuto", false) && (pos == SDController.Positions.THREE ||
                pos == SDController.Positions.FOUR) && (def == SDController.Defense.ROCK_WALL || def ==
                SDController.Defense.ROUGH_TERRAIN || def == SDController.Defense.MOAT)) {
            addParallel(new RaiseScooper());
            addSequential(new RaiseLift());
            addSequential(new DriveStraightBack());
        }
    }
}