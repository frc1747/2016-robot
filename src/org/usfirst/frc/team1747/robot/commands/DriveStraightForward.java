package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

public class DriveStraightForward extends Command {
    private DriveTrain driveTrain;
    private double startTime;
    private long defaultTime;

    public DriveStraightForward() {
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
    }

    // sets up tankDrive
    @Override
    protected void initialize() {
        SDController.Positions position = Robot.getSd().getAutonPosition();
        SDController.Defense defenseType = Robot.getSd().getDefenseType();
        defaultTime = 3200;
        if (position == SDController.Positions.ONE || position == SDController.Positions.FIVE) {
            defaultTime += 50;
        }
        else if (position == SDController.Positions.TWO || position == SDController.Positions.THREE || position == SDController.Positions.FOUR){
        	defaultTime -= 325;
        }
        if (defenseType == SDController.Defense.ROUGH_TERRAIN || defenseType == SDController.Defense.MOAT) {
            defaultTime -= 50;
        } else if (defenseType == SDController.Defense.ROCK_WALL) {
            defaultTime += 150;
        }
        driveTrain.tankDrive(.5, .525);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
    }

    // returns true if more than 3250 startTime has passed
    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() - startTime > defaultTime;
    }

    @Override
    protected void end() {
        driveTrain.tankDrive(0.0, 0.0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
