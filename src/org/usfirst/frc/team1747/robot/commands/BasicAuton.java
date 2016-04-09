package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;

public class BasicAuton extends CommandGroup {

    public BasicAuton() {
        SDController sd = Robot.getSd();
        SDController.Positions pos = sd.getAutonPosition();
        SDController.Defense def = sd.getDefenseType();
        if (def == SDController.Defense.PORTICULLIS || def == SDController.Defense.LOW_BAR) {
            addParallel(new LowerLift());
            addSequential(new LowerScooper());
        } else if (def == SDController.Defense.CHEVAL_DE_FRISE) {
            addSequential(new LowerScooper());
        }
        addSequential(new DriveStraightForward());
        addSequential(new RaiseLift());
        addSequential(new AutoShoot());
        if ((pos == SDController.Positions.THREE || pos == SDController.Positions.FOUR) &&
                (def == SDController.Defense.ROCK_WALL || def == SDController.Defense.ROUGH_TERRAIN
                        || def == SDController.Defense.MOAT)) {
            addParallel(new RaiseScooper());
            addSequential(new RaiseLift());
            addSequential(new DriveStraightBack());
        }
    }
}