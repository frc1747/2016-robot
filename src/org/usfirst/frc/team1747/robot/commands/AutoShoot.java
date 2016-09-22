package org.usfirst.frc.team1747.robot.commands;

import org.usfirst.frc.team1747.robot.Robot;
import org.usfirst.frc.team1747.robot.SDController;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrainPID;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;
import org.usfirst.frc.team1747.robot.subsystems.Flashlight;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShoot extends Command {

	private DriveTrain driveTrain;
	private Shooter shooter;
	private Flashlight flashlight;
	private Intake intake;
	private Scooper scooper;
	private NetworkTable networkTable;
	private double startTime;
	private SDController.Positions position;
	private double turnValue;
	private DriverStation driverStation;
	private DriveTrainPID drivePID;

	int count = 0;
	int countTarget = 16;

	private static final int angles_count = 50;
	private static final int angle_delay = 15;
	private double angles[];
	private int angle_index;
	
	private static final double angle_offset = -0.865 * Math.PI / 180;
	
	public AutoShoot() {
		driveTrain = Robot.getDriveTrain();
		shooter = Robot.getShooter();
		intake = Robot.getIntake();
		scooper = Robot.getScooper();
		flashlight = Robot.getFlashlight();
		drivePID = Robot.getDriveTrainPID();
		networkTable = NetworkTable.getTable("imageProcessing");
		driverStation = DriverStation.getInstance();
		requires(driveTrain);
		requires(shooter);
		requires(intake);
		requires(scooper);
		requires(flashlight);
		requires(drivePID);
	}

	protected void initialize() {
		position = Robot.getSd().getAutonPosition();
		startTime = -1;
		flashlight.turnOffFlashlight();
		driveTrain.resetGyro();
		turnValue = driveTrain.getAutonTurn();
		drivePID.pidDisable();
		
		angle_index = 0;
		angles = new double[angles_count];
	}

	protected void execute() {
		//Ensure scooper is at lower limit
		if (!scooper.isAtLowerLimit()) {
			scooper.moveScooperDown();
		} else {
			scooper.scooperStop();
		}
		
		//Populate angles array
		angle_index++;
		angle_index %= angles_count;
		angles[angle_index] = driveTrain.getTurnAngle();
		
		//For auton, make sure that the position is valid
		if (position != SDController.Positions.NOTHING) {
			//double turnAngle = SmartDashboard.getNumber("OffCenterDegreesX");
			
			//Getting distance and angle
			double realDr = SmartDashboard.getNumber("RealDr");
			double realXr = SmartDashboard.getNumber("RealXr");

			//Derived values
			double offAngle = Math.atan2(realXr, realDr);
			double offDist = Math.hypot(realXr, realDr);
			
			//Real angle needs to turn corrected for time and offset
			
			
			
			double xrTol = 2.625;
			double drTol = 5;
			double drTarget = 139;
			double xrTarget = -2.1;
			double angleOffset = Math.atan2(xrTarget, drTarget);
			double realTurnAngle = turnAngle + angleOffset;
			//String direction = networkTable.getString("ShootDirection", "robotUnknown");
			String direction = "unknown";
			if(SmartDashboard.getBoolean("targetFound")) {
				direction = "shoot";
				//Currently prioritizing left/right over forward/backward
				if(realDr > drTarget + drTol) direction = "forward";
				if(realDr < drTarget - drTol) direction = "backward";
				if(realXr > xrTarget + xrTol) direction = "left";
				if(realXr < xrTarget - xrTol) direction = "right";
			}
			
			if (SmartDashboard.getBoolean("LastSecondShot", false) && driverStation.isAutonomous()
					&& !direction.equals("robotUnknown") && driverStation.getMatchTime() < 3) {
				direction = "shoot";
			}
			SmartDashboard.putString("direction", direction);
			if(direction.equals("shoot") && (!drivePID.isPidEnabled() || drivePID.isAtTarget() || (drivePID.getPidOutput() < .08 && drivePID.getPidOutput() > -0.08))) {
				drivePID.pidDisable();
				count = 0;
				driveTrain.arcadeDrive(0, 0);
				if(!shooter.isPidEnabled()) {
					shooter.setSetpoint(shooter.getTargetShooterSpeed());
					shooter.pidEnable();
				}
				if(shooter.isAtTarget()) {
					intake.intakeBall();
					if(startTime == -1) startTime = System.currentTimeMillis();
				}
			}
			else {
				startTime = -1;
				shooter.pidDisable();
				shooter.setSpeed(0.0);
				if(direction.equals("left") || direction.equals("right")) {
					if (!drivePID.isPidEnabled()) {
						//driveTrain.arcadeDrive(0, 0);
						count++;
						System.out.println("Incrementing counter");
						if(count > countTarget) {
							driveTrain.resetGyro();
							//double cameraAngle = networkTable.getNumber("GyroAngle", 0.0) * 1.9;
							drivePID.setSetpoint(realTurnAngle);
							drivePID.pidEnable();
							count = 0;
							System.out.println("count = 0; location 1");
						}
					}
					if (drivePID.isAtTarget()) {
						drivePID.pidDisable();
						driveTrain.resetGyro();
						count = 0;
						System.out.println("count = 0; location 2");
					}
				} else {
					//if (drivePID.isPidEnabled() && drivePID.isAtTarget() || (drivePID.getPidOutput() < .10 && drivePID.getPidOutput() > -0.10)) {
					drivePID.pidDisable();
					count = 0;
					System.out.println("count = 0; location 3");
					//}
					//if (direction.equals("forward") && !drivePID.isPidEnabled()) {
					if (direction.equals("forward")) {
						driveTrain.tankDrive(0.25, 0.27);
					}
					//} else if (direction.equals("backward") && !drivePID.isPidEnabled()) {
					else if (direction.equals("backward")) {
						driveTrain.tankDrive(-0.25, -0.27);
					}
					//} else if (direction.equals("unknown") && !drivePID.isPidEnabled()) {
					else if (direction.equals("unknown")) {
						//drivePID.pidDisable();
						if (position == SDController.Positions.ONE || position == SDController.Positions.TWO
								|| position == SDController.Positions.THREE) {
							driveTrain.arcadeDrive(0, turnValue);
						} else {
							driveTrain.arcadeDrive(0, -turnValue);
						}
					}
				}
			}
			SmartDashboard.putNumber("drive pid count", count);	
		}
	}

	protected boolean isFinished() {
		return (startTime != -1 && System.currentTimeMillis() - startTime > 600) || position == SDController.Positions.NOTHING;
	}

	protected void end() {
		drivePID.pidDisable();
		count = 0;
		driveTrain.arcadeDrive(0, 0);
		shooter.setSpeed(0.0);
		shooter.pidDisable();
		intake.rollerStop();
	}

	protected void interrupted() {
		end();
	}
}