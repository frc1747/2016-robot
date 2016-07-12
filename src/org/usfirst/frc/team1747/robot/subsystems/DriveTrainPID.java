package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainPID extends Subsystem implements PIDSource, PIDOutput {

	private double kP, kI, kD, kF;
	DriveTrain driveTrain;
	PIDController pidController;
	private double cameraAngle;
	private double driveSetpoint;
	private boolean atTarget = false;
	private int count = 0;
	private static final double errorMargin = 1.0;

	public DriveTrainPID() {
		cameraAngle = -1.0;
		kP = 0.039;
		kI = 0.005;// 0.326;
		kD = 0.080;// 0.0815;
		kF = 0.0;
		pidController = new PIDController(kP, kI, kD, kF, this, this);
		pidController.setOutputRange(-0.5, 0.5);
		pidController.setAbsoluteTolerance(0.5);
		driveTrain = Robot.getDriveTrain();
		SmartDashboard.putData("PIDController", pidController);
	}

	protected void initDefaultCommand() {
	}

	public void pidWrite(double output) {
		/*if(output < 0 && output > -0.2) {
			output = Math.min(-0.2, output);
		}
		else if(output > 0 && output < 0.2) {
			output = Math.max(0.2, output);
		}*/
		SmartDashboard.putNumber("DRIVE PID Output", output);
		driveTrain.arcadeDrive(0, output);
	}

	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	public double pidGet() {
		SmartDashboard.putNumber("CAMERA_ANGLE", cameraAngle);
		return driveTrain.getTurnAngle();
	}

	public void setSetpoint(double setpoint) {
		driveSetpoint = setpoint;
		pidController.setSetpoint(setpoint);
	}

	public void pidEnable() {
		count = 0;
		pidController.enable();
	}

	public void pidDisable() {
		count = 0;
		pidController.disable();
	}

	public boolean isAtTarget() {
		if(Math.abs(driveSetpoint - driveTrain.getTurnAngle()) < errorMargin) {
			count++;
		}
		else {
			count = 0;
			atTarget = false;
		}
		if(count > 7) atTarget = true;
		SmartDashboard.putNumber("DRIVE PID COUNT", count);
		SmartDashboard.putBoolean("DRIVE PID IS AT TARGET", atTarget);
		return atTarget;
	}

	public void setCameraAngle(double newCameraAngle) {
		cameraAngle = newCameraAngle;
	}

	public boolean isPidEnabled() {
		return pidController.isEnabled();
	}
}
