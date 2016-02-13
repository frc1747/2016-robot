package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;

/**
 * Created by Anubhaw Arya on 2/13/16.
 */
public class LiftPortcullis extends Command {
    DriveTrain drive;
    Intake intake;
    SmartDashboard smartDashboard;

    LiftPortcullis() {
        drive = Robot.getDriveTrain();
        intake = Robot.getIntake();
        requires(drive);
        requires(intake);
    }

    @Override
    protected void initialize() {
        intake.enablePID();
        intake.setPID(SmartDashboard.getNumber("Portcullis P"), SmartDashboard.getNumber("Portcullis I"),
                SmartDashboard.getNumber("Portcullis D"));
        intake.setSetpoint(SmartDashboard.getNumber("Portcullis targetDistance"));
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return intake.isAtTarget();
    }

    @Override
    protected void end() {
        intake.disablePID();
    }

    @Override
    protected void interrupted() {

    }
}
