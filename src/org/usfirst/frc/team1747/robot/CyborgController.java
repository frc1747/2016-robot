package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CyborgController extends Cyborg{

	private static final int LEFT_JOY_HORIZ_AXIS = 0;
	private static final int LEFT_JOY_VERT_AXIS = 1;

	private static final int TRIGGER_AXIS = 2;

	private static final int RIGHT_JOY_HORIZ_AXIS = 3;
	private static final int RIGHT_JOY_VERT_AXIS = 4;

	private static final int JOY_X_BUTTON = 1;
	private static final int JOY_A_BUTTON = 2;
	private static final int JOY_B_BUTTON = 3;
	private static final int JOY_Y_BUTTON = 4;

	JoystickButton buttonA, buttonB, buttonX, buttonY;

	public CyborgController(int portNum) {
		super(portNum);
		buttonX = new JoystickButton(controller, JOY_X_BUTTON);
		buttonY = new JoystickButton(controller, JOY_Y_BUTTON);
		buttonA = new JoystickButton(controller, JOY_A_BUTTON);
		buttonB = new JoystickButton(controller, JOY_B_BUTTON);
	}
	
	public JoystickButton getButtonOne() {
		return buttonX;
	}

	public JoystickButton getButtonTwo() {
		return buttonA;
	}

	public JoystickButton getButtonThree() {
		return buttonB;
	}

	public JoystickButton getButtonFour() {
		return buttonY;
	}

	public double getLeftVert() {
		return -controller.getRawAxis(LEFT_JOY_VERT_AXIS);
	}

	public double getLeftHoriz() {
		return controller.getRawAxis(LEFT_JOY_HORIZ_AXIS);
	}

	public double getRightVert() {
		return -controller.getRawAxis(RIGHT_JOY_VERT_AXIS);
	}

	public double getRightHoriz() {
		return controller.getRawAxis(RIGHT_JOY_HORIZ_AXIS);
	}

	public double getTriggerAxis(){
		return -controller.getRawAxis(TRIGGER_AXIS);
	}

	public void logToSmartDashboard(){
		super.logToSmartDashboard();
		SmartDashboard.putBoolean("Button One",buttonX.get());
		SmartDashboard.putBoolean("Button Two",buttonA.get());
		SmartDashboard.putBoolean("Button Three",buttonB.get());
		SmartDashboard.putBoolean("Button Four",buttonY.get());
		SmartDashboard.putNumber("Left Joystick X", getLeftHoriz());
		SmartDashboard.putNumber("Left Joystick Y", getLeftVert());
		SmartDashboard.putNumber("Right Joystick X", getRightHoriz());
		SmartDashboard.putNumber("Right Joystick Y", getRightVert());
		SmartDashboard.putNumber("Trigger Axis", getTriggerAxis());
	}
}