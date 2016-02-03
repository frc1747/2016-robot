package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoShoot extends Command {
	
	DriveTrain drive;
	Shooter shoot;
	NetworkTable networkTable;
	
	public AutoShoot() {
		drive = Robot.getDrive();
		shoot = Robot.getShooter();
		networkTable = NetworkTable.getTable("imageProcessing");
		requires(shoot);
	}

	protected void initialize() {
		
	}

	protected void execute() {
		String direction = networkTable.getString("ShootDirection", "unknown");
		if(direction == "left"){
			drive.arcadeDrive(0.0, 0.3);
		}
		if(direction == "right"){
			drive.arcadeDrive(0.0, -0.3);
		}
		if(direction == "forward"){
			drive.arcadeDrive(0.3, 0.0);
		}
		if(direction == "backward"){
			drive.arcadeDrive(-0.3, 0.0);
		}
		if(direction == "shoot"){
			shoot.shoot(0.5);
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}

}
