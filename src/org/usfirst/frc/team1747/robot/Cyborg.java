package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class Cyborg {
	
	private static final int JOY_LEFT_BUMPER = 5;
	private static final int JOY_RIGHT_BUMPER = 6;
	private static final int TRIGGER_LEFT_BUTTON=7;
	private static final int TRIGGER_RIGHT_BUTTON=8;

	private static final int BACK_BUTTON=9;
	private static final int START_BUTTON=10;

	private static final int LEFT_JOY_PRESS=11;
	private static final int RIGHT_JOY_PRESS=12;

	private static final int FPS_BUTTON=13;
	
	JoystickButton leftBumper, rightBumper, leftTrigger, rightTrigger;
	JoystickButton leftJoystickPress, rightJoystickPress;
	JoystickButton fpsButton, backButton, startButton;
	
	Joystick controller;
	
	public Cyborg(int portNum){
		controller=new Joystick(portNum);
		leftBumper=new JoystickButton(controller,JOY_LEFT_BUMPER);
		rightBumper = new JoystickButton(controller, JOY_RIGHT_BUMPER);
		leftTrigger = new JoystickButton(controller, TRIGGER_LEFT_BUTTON);
		rightTrigger = new JoystickButton(controller, TRIGGER_RIGHT_BUTTON);
		leftJoystickPress = new JoystickButton(controller, LEFT_JOY_PRESS);
		rightJoystickPress = new JoystickButton(controller, RIGHT_JOY_PRESS);
		fpsButton = new JoystickButton(controller, FPS_BUTTON);
		backButton = new JoystickButton(controller, BACK_BUTTON);
		startButton = new JoystickButton(controller, START_BUTTON);	
	}
	
	public JoystickButton getLeftBumper() {
		return leftBumper;
	}

	public JoystickButton getRightBumper() {
		return rightBumper;
	}

	public JoystickButton getLeftTrigger() {
		return leftTrigger;
	}

	public JoystickButton getRightTrigger() {
		return rightTrigger;
	}

	public JoystickButton getLeftJoystickPress() {
		return leftJoystickPress;
	}

	public JoystickButton getRightJoystickPress() {
		return rightJoystickPress;
	}

	public JoystickButton getFpsButton() {
		return fpsButton;
	}

	public JoystickButton getStartButton() {
		return startButton;
	}
	
	public JoystickButton getBackButton() {
		return backButton;
	}
	
	public int getDPad(){
		return controller.getPOV();
	}

	abstract public JoystickButton getButtonOne();
	abstract public JoystickButton getButtonTwo();
	abstract public JoystickButton getButtonThree();
	abstract public JoystickButton getButtonFour();
	abstract public double getLeftVert() ;
	abstract public double getLeftHoriz() ;
	abstract public double getRightVert() ;
	abstract public double getRightHoriz() ;
	abstract public double getTriggerAxis();

	public void logToSmartDashboard() {
		SmartDashboard.putBoolean("Left Bumper", leftBumper.get());
		SmartDashboard.putBoolean("Right Bumper", rightBumper.get());
		SmartDashboard.putBoolean("Left Trigger", leftTrigger.get());
		SmartDashboard.putBoolean("Right Trigger", rightTrigger.get());
		SmartDashboard.putBoolean("L3", leftJoystickPress.get());
		SmartDashboard.putBoolean("R3", rightJoystickPress.get());
		SmartDashboard.putBoolean("FPS Button", fpsButton.get());
		SmartDashboard.putBoolean("Back Button", backButton.get());
		SmartDashboard.putBoolean("Start Button", startButton.get());
		SmartDashboard.putNumber("D-Pad", getDPad());
	}

}