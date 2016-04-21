package org.usfirst.frc.team1747.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1747.robot.PixyCamera;
import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

public class AutoShoot extends Command {

	private DriveTrain drive;
	private Shooter shoot;
	private Intake intake;
	private Scooper scooper;
	private PixyCamera pixyCamera;
	private SDController.Positions position;
	private double turnValue;
	private DriverStation driverStation;
	private long startTime;

	// sets up AutoShoot
	public AutoShoot() {
		drive = Robot.getDriveTrain();
		shoot = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		pixyCamera = Robot.getPixyCamera();
		driverStation = DriverStation.getInstance();
		requires(shoot);
		requires(drive);
		requires(intake);
		requires(scooper);
	}

	// initializes AutoShoot then prints out that it is running
	protected void initialize() {
		startTime = -1;
		position = Robot.getSd().getAutonPosition();
		turnValue = drive.getAutonTurn();
		drive.resetGyro();
		shoot.turnOffFlashlight();
	}

	protected void execute() {
		// Lowers scooper if not at lower limit
		if (!scooper.isAtLowerLimit()) {
			scooper.moveScooperDown();
		} else {
			scooper.scooperStop();
		}
		if (!intake.isAtTop()) {
			intake.moveLiftUp();
		} else {
			intake.liftStop();
		}
		if (position != SDController.Positions.NOTHING) {
			if (pixyCamera.shouldTurnLeft()) {
				if (shoot.isPidEnabled()) {
					shoot.disablePID();
					shoot.shoot(0);
				}
				startTime = -1;
				drive.arcadeDrive(0.0, (-turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
			} else if (pixyCamera.shouldTurnRight()) {
				if (shoot.isPidEnabled()) {
					shoot.disablePID();
					shoot.shoot(0);
				}
				startTime = -1;
				drive.arcadeDrive(0.0, (turnValue) * (driverStation.isAutonomous() ? 1 : 1.1));
			} else if (pixyCamera.shouldMoveForward()) {
				if (shoot.isPidEnabled()) {
					shoot.disablePID();
					shoot.shoot(0);
				}
				startTime = -1;
				drive.arcadeDrive(0.25, 0.0);
			} else if (pixyCamera.shouldMoveBackward()) {
				if (shoot.isPidEnabled()) {
					shoot.disablePID();
					shoot.shoot(0);
				}
				startTime = -1;
				drive.arcadeDrive(-0.25, 0.0);
			} else if (pixyCamera.shouldShoot()) {
				drive.arcadeDrive(0, 0);
				if (startTime == -1) {
					startTime = System.currentTimeMillis();
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 500 && !shoot.isPidEnabled()) {
					shoot.setSetpoint(shoot.getTargetShooterSpeed());
					shoot.enablePID();
				}
				if (shoot.isPidEnabled()) {
					shoot.runPID();
				}
				if (startTime != -1 && System.currentTimeMillis() - startTime > 750 && shoot.isAtTarget()) {
					intake.intakeBall();
				}
			} else {
				if (position == SDController.Positions.ONE || position == SDController.Positions.TWO
						|| position == SDController.Positions.THREE) {
					drive.arcadeDrive(0, 1.3 * turnValue);
				} else {
					drive.arcadeDrive(0, 1.3 * -turnValue);
				}
			}
		}
	}

	// returns true if auto mode is done, if not it returns false; uses
	// startTime, position, and the current system time
	protected boolean isFinished() {
		return (startTime != -1 && System.currentTimeMillis() - startTime > 2250)
				|| position == SDController.Positions.NOTHING;
	}

	// ends shoot and arcadeDrive
	protected void end() {
		drive.arcadeDrive(0, 0);
		shoot.shoot(0);
	}

	protected void interrupted() {
		end();
	}
}
