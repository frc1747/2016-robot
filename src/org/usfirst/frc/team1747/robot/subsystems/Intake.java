package org.usfirst.frc.team1747.robot.subsystems;

import org.usfirst.frc.team1747.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    CANTalon leftLiftMotor, rightLiftMotor, rollerMotor;
    public static DigitalInput liftInput, intakeInput;
    
    public Intake(){
    	leftLiftMotor = new CANTalon(RobotMap.LEFT_LIFT_MOTOR);
    	rightLiftMotor = new CANTalon(RobotMap.RIGHT_LIFT_MOTOR);
    	rollerMotor = new CANTalon(RobotMap.ROLLER_MINICIM);
    }
    
    //Moves the arm
    public void liftControl(double speed){
    	leftLiftMotor.set(-speed);
    	rightLiftMotor.set(speed);
    }
    
    //Sets the pickup speed
    public void rollerControl(double speed){
    	rollerMotor.set(speed);
    }
    
    public DigitalInput getLiftInput(){
    	return liftInput;
    }
    
    public DigitalInput getIntakeInput(){
    	return intakeInput;
    }


    public void initDefaultCommand() {
    }
}

