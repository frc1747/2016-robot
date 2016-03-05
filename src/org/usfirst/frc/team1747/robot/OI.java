package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.AutoShoot;
import org.usfirst.frc.team1747.robot.commands.BallEject;
import org.usfirst.frc.team1747.robot.commands.DriveStraight;
import org.usfirst.frc.team1747.robot.commands.IntakeBall;
import org.usfirst.frc.team1747.robot.commands.LowGoalShoot;
import org.usfirst.frc.team1747.robot.commands.LowerLift;
import org.usfirst.frc.team1747.robot.commands.RaiseLift;
import org.usfirst.frc.team1747.robot.commands.Shoot;
import org.usfirst.frc.team1747.robot.commands.TurnToAngle;

public class OI {

	private CyborgController controller;
	private PrecisionCyborgController auxController;

	public OI() {
		controller = new CyborgController(0);
		auxController = new PrecisionCyborgController(1);
		controller.getRightTrigger().whileHeld(new Shoot());
		controller.getLeftBumper().toggleWhenPressed(new IntakeBall());
		controller.getButtonTwo().whenPressed(new LowerLift());
		controller.getButtonThree().toggleWhenPressed(new BallEject());
		controller.getButtonFour().whenPressed(new RaiseLift());
		controller.getStartButton().whileHeld(new AutoShoot());
		controller.getRightBumper().whileHeld(new LowGoalShoot());
		controller.getBackButton().whenPressed(new DriveStraight());
		auxController.getButtonOne().toggleWhenPressed(new IntakeBall());
		auxController.getButtonTwo().whenPressed(new LowerLift());
		auxController.getButtonThree().toggleWhenPressed(new BallEject());
		auxController.getButtonFour().whenPressed(new RaiseLift());
		auxController.getRightTrigger().whenPressed(new TurnToAngle());
	}

	public CyborgController getController() {
		return controller;
	}

	public PrecisionCyborgController getAuxController() {
		return auxController;
	}

}