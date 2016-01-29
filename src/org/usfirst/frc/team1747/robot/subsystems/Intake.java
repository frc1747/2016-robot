package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem {

	CANTalon leftLiftMotor, rightLiftMotor, rollerMotor;
	DigitalInput bottomIntake, topIntake, ballIntake;

	public Intake() {
		leftLiftMotor = new CANTalon(RobotMap.LEFT_LIFT_MOTOR);
		rightLiftMotor = new CANTalon(RobotMap.RIGHT_LIFT_MOTOR);
		rightLiftMotor.setInverted(true);
		rollerMotor = new CANTalon(RobotMap.ROLLER_MINICIM);
		bottomIntake = new DigitalInput(RobotMap.BOTTOM_INTAKE);
		topIntake = new DigitalInput(RobotMap.TOP_INTAKE);
		ballIntake = new DigitalInput(RobotMap.BALL_INTAKE);
	}

	// Moves the arm
	public void liftControl(double speed) {
		if ((speed > 0 && !isAtTop()) || (speed < 0 && !isAtBottom())) {
			leftLiftMotor.set(speed);
			rightLiftMotor.set(speed);
		} else {
			leftLiftMotor.set(0);
			rightLiftMotor.set(0);
		}
	}

	public void moveLiftDown() {
		liftControl(.5);
	}

	public void moveLiftUp() {
		liftControl(-.5);
	}

	// Sets the pickup speed
	public void rollerControl(double speed) {
		rollerMotor.set(speed);
	}

	public void initDefaultCommand() {
	}

	public boolean isAtBottom() {
		return bottomIntake.get();
	}

	public boolean isAtTop() {
		return topIntake.get();
	}

	public boolean hasBall() {
		return ballIntake.get();
	}

	public void logToSmartDashboard() {
		SmartDashboard.putBoolean("TopIntake", isAtTop());
		SmartDashboard.putBoolean("BottomIntake", isAtBottom());
		SmartDashboard.putBoolean("BallIntake", hasBall());

	}
}