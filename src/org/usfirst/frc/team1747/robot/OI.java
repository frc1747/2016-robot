package org.usfirst.frc.team1747.robot;

import org.usfirst.frc.team1747.robot.commands.BallEject;
import org.usfirst.frc.team1747.robot.commands.IntakeBall;
import org.usfirst.frc.team1747.robot.commands.IntakeManual;
import org.usfirst.frc.team1747.robot.commands.LowerLift;
import org.usfirst.frc.team1747.robot.commands.RaiseLift;
import org.usfirst.frc.team1747.robot.commands.Shoot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class OI {

	private CyborgController controller;

	public OI() {
		getController().getRightTrigger().whileHeld(new Shoot(SmartDashboard.getNumber("Shooter Speed", 1.0)));
		getController().getButtonOne().whenPressed(new LowerLift());
		getController().getButtonTwo().whenPressed(new IntakeBall());
		getController().getButtonThree().whenPressed(new BallEject());
		getController().getButtonFour().whenPressed(new RaiseLift());
	}

	public CyborgController getController() {
		if (controller == null) {
			controller = new CyborgController(0);
		}
		return controller;
	}
	
	private USBCamera intakeCamera;
	
	public USBCamera getIntakeCamera() {
		if (intakeCamera == null){
			intakeCamera = new USBCamera();
			intakeCamera.openCamera(); 
			
		}
		return intakeCamera;
	}
	private USBCamera shooterCamera;
	
	public USBCamera getShooterCamera() {
		if (shooterCamera == null){
			shooterCamera = new USBCamera();
			shooterCamera.openCamera();
			shooterCamera.startCapture();
		}
		return shooterCamera;
	}
}