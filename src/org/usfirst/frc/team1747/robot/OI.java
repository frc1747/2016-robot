package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.Shoot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {

	private CyborgController controller;

	public OI() {
		getController().buttonA.whenPressed(new Shoot(SmartDashboard.getNumber("Shooter Speed", 1.0)));
		getController().buttonA.cancelWhenPressed(new Shoot(0.0));
	}

	public CyborgController getController() {
		if (controller == null) {
			controller = new CyborgController(0);
		}
		return controller;
	}
}