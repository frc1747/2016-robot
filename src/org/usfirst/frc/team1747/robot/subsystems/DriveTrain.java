package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	CANTalon leftCimOne;
	CANTalon leftCimTwo;
	CANTalon leftMiniCim;
	CANTalon rightCimOne;
	CANTalon rightCimTwo;
	CANTalon rightMiniCim;
	
	static final double[] SIGMOIDSTRETCH = {0.03, 0.06, 0.09, 0.1, 
			0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03};
	
	double[] leftTargetDeltas = new double[SIGMOIDSTRETCH.length];
	double[] rightTargetDeltas = new double[SIGMOIDSTRETCH.length];
	
	double pLeftCurrent, pRightCurrent, prevLeftTarget, prevRightTarget;

	public DriveTrain() {
		leftCimOne = new CANTalon(RobotMap.LEFT_DRIVE_CIM_ONE);
		leftCimTwo = new CANTalon(RobotMap.LEFT_DRIVE_CIM_TWO);
		leftMiniCim = new CANTalon(RobotMap.LEFT_DRIVE_MINICIM);
		rightCimOne = new CANTalon(RobotMap.RIGHT_DRIVE_CIM_ONE);
		rightCimTwo = new CANTalon(RobotMap.RIGHT_DRIVE_CIM_TWO);
		rightMiniCim = new CANTalon(RobotMap.RIGHT_DRIVE_MINICIM);
		for(int j = 0; j < SIGMOIDSTRETCH.length; j++){
			leftTargetDeltas[j] = 0.0;
			rightTargetDeltas[j] = 0.0;
		}
		prevLeftTarget = 0.0;
		prevRightTarget = 0.0;
		pLeftCurrent = 0.0;
		pRightCurrent = 0.0;
	}

	public void tankDrive(double leftSpeed, double rightSpeed){
		leftCimOne.set(leftSpeed);
		leftCimTwo.set(leftSpeed);
		leftMiniCim.set(leftSpeed);
		rightCimOne.set(rightSpeed);
		rightCimTwo.set(rightSpeed);
		rightMiniCim.set(rightSpeed);
	}
	
	public void arcadeDrive(double straight, double turn){
			tankDrive(straight+turn, straight-turn);
	}
	
	public void smoothDrive(double targetLeftCurrent, double targetRightCurrent){
		for(int i = leftTargetDeltas.length-1; i > 0; i--){
			leftTargetDeltas[i] = leftTargetDeltas[i-1];
			rightTargetDeltas[i] = rightTargetDeltas[i-1];
		}
		leftTargetDeltas[0] = targetLeftCurrent - prevLeftTarget;
		rightTargetDeltas[0] = targetRightCurrent - prevRightTarget;
		prevLeftTarget = targetLeftCurrent;
		prevRightTarget = targetRightCurrent;
		for(int i = 0; i< SIGMOIDSTRETCH.length; i++){
			pLeftCurrent += leftTargetDeltas[i]*SIGMOIDSTRETCH[i];
			pRightCurrent += rightTargetDeltas[i]*SIGMOIDSTRETCH[i];
		}
		arcadeDrive(pLeftCurrent, pRightCurrent);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive());
		
	}

	public void logToSmartDashboard() {
	}
}
