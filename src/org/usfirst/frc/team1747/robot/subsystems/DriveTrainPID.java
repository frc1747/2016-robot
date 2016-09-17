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
	private double pidOutput;
	private static final double errorMargin = 0.7;
	//private static double minPower = 0.2;

	public DriveTrainPID() {
		cameraAngle = -1.0;
		kP = 0.050;
		kI = 0.005;
		kD = 0.095;
		kF = 0.0;
		pidController = new PIDController(kP, kI, kD, kF, this, this);
		pidController.setOutputRange(-0.5, 0.5);
		pidController.setAbsoluteTolerance(0.5);
		driveTrain = Robot.getDriveTrain();
		SmartDashboard.putData("DRIVE PID Controller", pidController);
		//SmartDashboard.putNumber("Drive Min Power", minPower);
	}

	protected void initDefaultCommand() {
	}

	public void pidWrite(double output) {
		/*minPower = SmartDashboard.getNumber("Drive Min Power", minPower);
		if(output < 0 && output > - minPower) {
			output = - minPower;
		}
		else if(output > 0 && output < minPower) {
			output = minPower;
		}*/
		SmartDashboard.putNumber("DRIVE PID Output", output);
		pidOutput = output;
		driveTrain.arcadeDrive(0, output);
	}
	
	public double getPidOutput() {
		return pidOutput;
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
		SmartDashboard.putNumber("DRIVE PID SETPOINT", setpoint);
		pidController.setSetpoint(setpoint);
	}

	public void pidEnable() {
		count = 0;
		pidController.enable();
	}

	public void pidDisable() {
		count = 0;
		pidController.reset();
	}

	public boolean isAtTarget() {
		if(Math.abs(driveSetpoint - driveTrain.getTurnAngle()) < errorMargin) {
			count++;
		}
		else {
			count = 0;
			atTarget = false;
		}
		if(count > 10) atTarget = true;
		SmartDashboard.putNumber("DRIVE PID COUNT", count);
		SmartDashboard.putBoolean("DRIVE PID IS AT TARGET", atTarget);
		return atTarget;
	}

	public void setCameraAngle(double newCameraAngle) {
		cameraAngle = newCameraAngle;
		/*if(Math.abs(cameraAngle) > 5.0) {
			pidController.setPID(kP, 0, kD);
		}
		else {
			pidController.setPID(kP, kI, kD);
		}*/
	}

	public boolean isPidEnabled() {
		return pidController.isEnabled();
	}
}
