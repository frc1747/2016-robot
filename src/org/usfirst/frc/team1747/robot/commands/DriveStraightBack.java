package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;

public class DriveStraightBack extends Command {
    private DriveTrain driveTrain;
    private double startTime;
    private long defaultTime;


    public DriveStraightBack() {
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
    }

    // sets up tankDrive
    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        driveTrain.tankDrive(-.5, -.5);
        SDController.Defense defenseType = Robot.getSd().getDefenseType();
        defaultTime = 1500;
        if (defenseType == SDController.Defense.MOAT || defenseType == SDController.Defense.ROUGH_TERRAIN) {
            defaultTime -= 50;
        }
    }

    @Override
    protected void execute() {
    }

    // returns true if more than 1500 startTime has passed
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
