package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.AutoShoot;
import org.usfirst.frc.team1747.robot.commands.BallEject;
import org.usfirst.frc.team1747.robot.commands.IntakeBall;
import org.usfirst.frc.team1747.robot.commands.LowGoalShoot;
import org.usfirst.frc.team1747.robot.commands.LowerLift;
import org.usfirst.frc.team1747.robot.commands.RaiseLift;
import org.usfirst.frc.team1747.robot.commands.Shoot;

public class OI {

	private CyborgController controller;
	private PrecisionCyborgController auxController;

	public OI() {
		controller = new CyborgController(0);
		auxController = new PrecisionCyborgController(1);
		controller.getRightTrigger().whileHeld(new Shoot());
		controller.getLeftTrigger().whileHeld(new IntakeBall());
		controller.getButtonTwo().whenPressed(new LowerLift());
		controller.getButtonThree().whenPressed(new BallEject());
		controller.getButtonFour().whenPressed(new RaiseLift());
		controller.getStartButton().whileHeld(new AutoShoot());
		controller.getRightBumper().whileHeld(new LowGoalShoot());
	}

	public CyborgController getController() {
		return controller;
	}

	public PrecisionCyborgController getAuxController() {
		return auxController;
	}

}