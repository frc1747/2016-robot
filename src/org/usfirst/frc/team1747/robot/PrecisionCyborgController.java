package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PrecisionCyborgController extends Cyborg{

	private static final int LEFT_JOY_HORIZ_AXIS = 0;
	private static final int LEFT_JOY_VERT_AXIS = 1;

	private static final int RIGHT_JOY_HORIZ_AXIS = 2;
	private static final int RIGHT_JOY_VERT_AXIS = 4;

	private static final int TRIGGER_AXIS = 3;
	
	private static final int JOY_SQUARE_BUTTON = 1;
	private static final int JOY_X_BUTTON = 2;
	private static final int JOY_CIRCLE_BUTTON = 3;
	private static final int JOY_TRIANGLE_BUTTON = 4;

	JoystickButton buttonX, buttonCircle, buttonSquare, buttonTriangle;

	public PrecisionCyborgController(int portNum) {
		super(portNum);
		buttonSquare = new JoystickButton(controller, JOY_SQUARE_BUTTON);
		buttonTriangle = new JoystickButton(controller, JOY_TRIANGLE_BUTTON);
		buttonX = new JoystickButton(controller, JOY_X_BUTTON);
		buttonCircle = new JoystickButton(controller, JOY_CIRCLE_BUTTON);
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

	public JoystickButton getButtonOne() {
		return buttonSquare;
	}

	public JoystickButton getButtonThree() {
		return buttonCircle;
	}

	public JoystickButton getButtonTwo() {
		return buttonX;
	}

	public JoystickButton getButtonFour() {
		return buttonTriangle;
	}

	public void logToSmartDashboard(){
		super.logToSmartDashboard();
		SmartDashboard.putBoolean("Button One",buttonSquare.get());
		SmartDashboard.putBoolean("Button Two",buttonX.get());
		SmartDashboard.putBoolean("Button Three",buttonCircle.get());
		SmartDashboard.putBoolean("Button Four",buttonTriangle.get());
		SmartDashboard.putNumber("Left Joystick X", getLeftHoriz());
		SmartDashboard.putNumber("Left Joystick Y", getLeftVert());
		SmartDashboard.putNumber("Right Joystick X", getRightHoriz());
		SmartDashboard.putNumber("Right Joystick Y", getRightVert());
		SmartDashboard.putNumber("Trigger Axis", getTriggerAxis());
	}
}