package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.ScooperManual;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Scooper extends Subsystem implements SDLogger {

	private Talon scooperMotor;
	private DigitalInput upperLimitSwitch;
	private DigitalInput lowerLimitSwitch;

	public Scooper() {
		scooperMotor = new Talon(RobotMap.SCOOPER_MOTOR);
		upperLimitSwitch = new DigitalInput(RobotMap.UPPER_SCOOPER_LIMIT);
		lowerLimitSwitch = new DigitalInput(RobotMap.LOWER_SCOOPER_LIMIT);
	}

	private void scooperControl(double speed) {
		if (isAtUpperLimit() && speed > 0) {
			speed = 0;
		} else if (isAtLowerLimit() && speed < 0) {
			speed = 0;
		}
		scooperMotor.set(speed);
	}

	public void moveScooperUp() {
		scooperControl(0.75);
	}

	public void moveScooperDown() {
		scooperControl(-0.75);
	}

	public void scooperStop() {
		scooperControl(0);
	}

	public boolean isAtUpperLimit() {
		return upperLimitSwitch.get();
	}

	public boolean isAtLowerLimit() {
		return lowerLimitSwitch.get();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new ScooperManual());
	}

	@Override
	public void logToSmartDashboard() {
		SmartDashboard.putBoolean("TopScooper", isAtUpperLimit());
		SmartDashboard.putBoolean("BottomScooper", isAtLowerLimit());
	}

}
