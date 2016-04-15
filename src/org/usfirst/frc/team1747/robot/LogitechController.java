package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LogitechController implements SDLogger {

	private static final int JOY_LEFT_BUMPER = 5;
	private static final int JOY_RIGHT_BUMPER = 6;

	private static final int BACK_BUTTON = 9;
	private static final int START_BUTTON = 10;

	private static final int LEFT_JOY_PRESS = 11;
	private static final int RIGHT_JOY_PRESS = 12;

	private static final int LEFT_JOY_HORIZ_AXIS = 0;
	private static final int LEFT_JOY_VERT_AXIS = 1;

	private static final int LEFT_TRIGGER = 7;
	private static final int RIGHT_TRIGGER = 8;

	private static final int RIGHT_JOY_HORIZ_AXIS = 4;
	private static final int RIGHT_JOY_VERT_AXIS = 5;

	private static final int JOY_X_BUTTON = 1;
	private static final int JOY_A_BUTTON = 2;
	private static final int JOY_B_BUTTON = 3;
	private static final int JOY_Y_BUTTON = 4;

	private Joystick controller;
	private JoystickButton buttonA;
	private JoystickButton buttonB;
	private JoystickButton buttonX;
	private JoystickButton buttonY;
	private JoystickButton leftBumper;
	private JoystickButton rightBumper;
	private JoystickButton leftTrigger;
	private JoystickButton rightTrigger;
	private JoystickButton backButton;
	private JoystickButton startButton;
	private JoystickButton leftJoystickButton;
	private JoystickButton rightJoystickButton;

	public LogitechController(int portNum) {
		controller = new Joystick(portNum);
		buttonA = new JoystickButton(controller, JOY_A_BUTTON);
		buttonB = new JoystickButton(controller, JOY_B_BUTTON);
		buttonX = new JoystickButton(controller, JOY_X_BUTTON);
		buttonY = new JoystickButton(controller, JOY_Y_BUTTON);
		backButton = new JoystickButton(controller, BACK_BUTTON);
		startButton = new JoystickButton(controller, START_BUTTON);
		leftBumper = new JoystickButton(controller, JOY_LEFT_BUMPER);
		rightBumper = new JoystickButton(controller, JOY_RIGHT_BUMPER);
		leftTrigger = new JoystickButton(controller, LEFT_TRIGGER);
		rightTrigger = new JoystickButton(controller, RIGHT_TRIGGER);
		leftJoystickButton = new JoystickButton(controller, LEFT_JOY_PRESS);
		rightJoystickButton = new JoystickButton(controller, RIGHT_JOY_PRESS);
	}

	public JoystickButton getA() {
		return buttonA;
	}

	public JoystickButton getB() {
		return buttonB;
	}

	public JoystickButton getX() {
		return buttonX;
	}

	public JoystickButton getY() {
		return buttonY;
	}

	public JoystickButton getStart() {
		return startButton;
	}

	public JoystickButton getBack() {
		return backButton;
	}

	public JoystickButton getHome() {
		return buttonX;
	}

	public JoystickButton getLeftBumper() {
		return leftBumper;
	}

	public JoystickButton getRightBumper() {
		return rightBumper;
	}

	public double getLeftVert() {
		return -controller.getRawAxis(LEFT_JOY_VERT_AXIS);
	}

	private double getLeftHoriz() {
		return controller.getRawAxis(LEFT_JOY_HORIZ_AXIS);
	}

	private double getRightVert() {
		return -controller.getRawAxis(RIGHT_JOY_VERT_AXIS);
	}

	public double getRightHoriz() {
		return controller.getRawAxis(RIGHT_JOY_HORIZ_AXIS);
	}

	public JoystickButton getLeftTrigger() {
		return leftTrigger;
	}

	public JoystickButton getRightTrigger() {
		return rightTrigger;
	}

	public int getDPad() {
		return controller.getPOV();
	}

	public JoystickButton getLeftJoystickButton() {
		return leftJoystickButton;
	}

	public JoystickButton getRightJoystickButton() {
		return rightJoystickButton;
	}

	@Override
	public void logToSmartDashboard() {
		SmartDashboard.putNumber("LeftJoyVert", getLeftVert());
		SmartDashboard.putNumber("LeftJoyHoriz", getLeftHoriz());
		SmartDashboard.putNumber("RightJoyVert", getRightVert());
		SmartDashboard.putNumber("RightJoyHoriz", getRightHoriz());
		SmartDashboard.putBoolean("Button A", buttonA.get());
		SmartDashboard.putBoolean("Button B", buttonB.get());
		SmartDashboard.putBoolean("Button X", buttonX.get());
		SmartDashboard.putBoolean("Button Y", buttonY.get());
		SmartDashboard.putBoolean("Start Button", startButton.get());
		SmartDashboard.putBoolean("Back Button", backButton.get());
		SmartDashboard.putNumber("D Pad", controller.getPOV());

	}
}
