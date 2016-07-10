package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainPID extends Subsystem implements PIDSource, PIDOutput {

	double kP, kI, kD, kF;
	DriveTrain drive;
	PIDController pidController;
	double cameraAngle;

	public DriveTrainPID() {
		cameraAngle = -1.0;
		kP = 0.039;
		kI = 0.005;// 0.326;
		kD = 0.08;// 0.0815;
		kF = 0.0;
		pidController = new PIDController(kP, kI, kD, kF, this, this);
		pidController.setOutputRange(-0.5, 0.5);
		pidController.setAbsoluteTolerance(0.5);
		drive = Robot.getDriveTrain();
		SmartDashboard.putData("PIDController", pidController);
	}

	protected void initDefaultCommand() {
	}

	public void pidWrite(double output) {
		SmartDashboard.putNumber("PIDOutPut", output);
		drive.arcadeDrive(0, output);
	}

	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	public double pidGet() {
		SmartDashboard.putNumber("CAMERA_ANGLE", cameraAngle);
		return drive.getTurnAngle();
	}

	public void setSetPoint(double setPoint) {
		pidController.setSetpoint(setPoint);
	}

	public void pidEnable() {
		pidController.enable();
	}

	public void pidDisable() {
		pidController.disable();
	}

	public boolean isAtTarget() {
		return pidController.onTarget();
	}

	public void setCameraAngle(double newCameraAngle) {
		cameraAngle = newCameraAngle;
	}

	public boolean isPidEnabled() {
		return pidController.isEnabled();
	}
}
