package org.usfirst.frc.team1747.robot;


import org.usfirst.frc.team1747.robot.commands.Shoot;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {

	static CyborgController controller;
	
	
	public OI() {
	}

	public void init() {
		
		
		controller.buttonA.whenPressed(new Shoot(SmartDashboard.getNumber("Shooter Speed", 1.0)));
		controller.buttonA.cancelWhenPressed(new Shoot(0.0));
	}
	
	public static CyborgController getController(){
		if(controller == null){
			controller = new CyborgController(0);
		}
		return controller;
	}
}
