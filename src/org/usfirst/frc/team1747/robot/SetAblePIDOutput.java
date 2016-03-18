package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class SetAblePIDOutput implements PIDOutput {

	double outputPower = 0;

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		outputPower = output;
	}

	public double getOutputPower() {
		return outputPower;
	}

}
