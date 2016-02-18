package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.RobotMap;
import org.usfirst.frc.team1747.robot.SDLogger;
import org.usfirst.frc.team1747.robot.commands.IntakeManual;


public class Intake extends Subsystem implements SDLogger {

    CANTalon leftLiftMotor, rightLiftMotor;
    Talon rollerMotor;
    DigitalInput ballIntake;
    Encoder encoder;
    double kP, kI, kD;
    double targetDistance;

    public Intake() {
        leftLiftMotor = new CANTalon(RobotMap.LEFT_LIFT_MOTOR);
        rightLiftMotor = new CANTalon(RobotMap.RIGHT_LIFT_MOTOR);
        rollerMotor = new Talon(RobotMap.ROLLER_MINICIM);
        ballIntake = new DigitalInput(RobotMap.BALL_INTAKE);
        encoder = new Encoder(RobotMap.INTAKE_ENCODER_A, RobotMap.INTAKE_ENCODER_B);
        leftLiftMotor.changeControlMode(TalonControlMode.Voltage);
        rightLiftMotor.changeControlMode(TalonControlMode.Follower);
        rightLiftMotor.set(RobotMap.LEFT_LIFT_MOTOR);
        SmartDashboard.putNumber("Portcullis P", kP);
        SmartDashboard.putNumber("Portcullis I", kI);
        SmartDashboard.putNumber("Portcullis D", kD);
    }

    // Moves the arm
    public void liftControl(double speed) {
        speed *= 12.0;
        if ((speed > 0 && !isAtTop())) {
            leftLiftMotor.set(speed);
        } else if (speed < 0 && !isAtBottom()) {
            leftLiftMotor.set(speed);
        } else {
            leftLiftMotor.set(0);
        }
    }

    public void moveLiftDown() {
        liftControl(-.25);
    }

    public void moveLiftUp() {
        liftControl(.5);
    }

    public void liftStop() {
        liftControl(0);
    }

    public void intakeBall() {
        rollerControl(1);
    }

    public void ejectBall() {
        rollerControl(-1);
    }

    public void rollerStop() {
        rollerMotor.set(0);
    }

    // Sets the pickup speed
    public void rollerControl(double speed) {
        rollerMotor.set(speed);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new IntakeManual());
    }

    public boolean isAtBottom() {
        return leftLiftMotor.isRevLimitSwitchClosed() && rightLiftMotor.isRevLimitSwitchClosed();
    }

    public boolean isAtTop() {
        return leftLiftMotor.isFwdLimitSwitchClosed() && rightLiftMotor.isFwdLimitSwitchClosed();
    }

    public boolean hasBall() {
        return !ballIntake.get();
    }

    public void logToSmartDashboard() {
        SmartDashboard.putBoolean("TopIntake", isAtTop());
        SmartDashboard.putBoolean("BottomIntake", isAtBottom());
        SmartDashboard.putBoolean("BallIntake", hasBall());
        SmartDashboard.putNumber("Intake Lift Speed", getSpeed());
        setPID(SmartDashboard.getNumber("Portcullis P", kP), SmartDashboard.getNumber("Portcullis I", kI),
                SmartDashboard.getNumber("Portcullis D", kD));
    }

    //returns encoder speed
    public double getSpeed() {
        return encoder.getRate();
    }

    //sets up PID
    public void setPID(double p, double i, double d) {
        kP = p;
        kI = i;
        kD = d;
    }

    public void setSetpoint(double targetDistance) {
        this.targetDistance = targetDistance;
        leftLiftMotor.setSetpoint(targetDistance);
        leftLiftMotor.setSetpoint(targetDistance);
    }

    //enables PID; changes the talon control mode for the left and right lift motors
    public void enablePID() {
        leftLiftMotor.changeControlMode(TalonControlMode.Position);
        rightLiftMotor.changeControlMode(TalonControlMode.Position);
        leftLiftMotor.setPID(kP, kI, kD);
        rightLiftMotor.setPID(kP, kI, kD);
    }

    //disables PID; changes the talon control modes for the left and right lift motors
    public void disablePID() {
        leftLiftMotor.changeControlMode(TalonControlMode.Voltage);
        rightLiftMotor.changeControlMode(TalonControlMode.Voltage);
        leftLiftMotor.setPID(0, 0, 0);
        rightLiftMotor.setPID(0, 0, 0);
    }

    //returns true if at target, false if not at target
    public boolean isAtTarget() {
        return Math.abs(this.targetDistance - leftLiftMotor.get()) < .01;
    }

}