package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.commands.TeleopDrive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	CANTalon leftTalon;
	CANTalon rightTalon;
	CANTalon leftTalon2;
	CANTalon rightTalon2;
	static final double[] SIGMOIDSTRETCH = {0.03, 0.06, 0.09, 0.1, 
			0.11, 0.12, 0.11, 0.1, 0.09, 0.06, 0.03};
	double[] leftTargetDeltas = new double[SIGMOIDSTRETCH.length];
	double[] rightTargetDeltas = new double[SIGMOIDSTRETCH.length];
	double pLeftCurrent, pRightCurrent, prevLeftTarget, prevRightTarget;

	public DriveTrain() {
		leftTalon = new CANTalon(1);
		rightTalon = new CANTalon(2);
		leftTalon2 = new CANTalon(22);
		rightTalon2 = new CANTalon(11);
		for(int j = 0; j < SIGMOIDSTRETCH.length; j++){
			leftTargetDeltas[j] = 0.0;
			rightTargetDeltas[j] = 0.0;
		}
		prevLeftTarget = 0.0;
		prevRightTarget = 0.0;
		pLeftCurrent = 0.0;
		pRightCurrent = 0.0;
	}
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void tankDrive(double leftSpeed, double rightSpeed){
		leftTalon.set(-leftSpeed);
		rightTalon.set(rightSpeed);
		leftTalon2.set(-leftSpeed);
		rightTalon2.set(rightSpeed);
		
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
}
