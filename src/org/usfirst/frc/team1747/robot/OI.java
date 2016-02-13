package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.AutoShoot;
import org.usfirst.frc.team1747.robot.commands.BallEject;
import org.usfirst.frc.team1747.robot.commands.IntakeBall;
import org.usfirst.frc.team1747.robot.commands.LowGoalShoot;
import org.usfirst.frc.team1747.robot.commands.LowerLift;
import org.usfirst.frc.team1747.robot.commands.RaiseLift;
import org.usfirst.frc.team1747.robot.commands.ReverseShooter;
import org.usfirst.frc.team1747.robot.commands.Shoot;
import org.usfirst.frc.team1747.robot.commands.StopIntake;

import edu.wpi.first.wpilibj.command.Command;

public class OI {

	private CyborgController controller;
	Command intake, cancelIntake;

	public OI() {
		controller = new CyborgController(0);
		intake = new IntakeBall();
		cancelIntake = new StopIntake();
		controller.getRightTrigger().whileHeld(new Shoot());
		controller.getLeftTrigger().whileHeld(new ReverseShooter());
		controller.getButtonOne().whenPressed(intake);
		controller.getButtonTwo().whenPressed(new LowerLift());
		controller.getButtonThree().whenPressed(new BallEject());
		controller.getButtonFour().whenPressed(new RaiseLift());
		controller.getStartButton().whileHeld(new AutoShoot());
		controller.getRightBumper().whileHeld(new LowGoalShoot());
	}

	public CyborgController getController() {
		return controller;
	}

	public void setIntakeCommand() {
		controller.getButtonOne().whenPressed(intake);
	}

	public void setCancelIntakeCommand() {
		controller.getButtonOne().whenPressed(cancelIntake);
	}
}