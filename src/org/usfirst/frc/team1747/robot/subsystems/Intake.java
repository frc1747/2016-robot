package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    CANTalon liftMotorOne, liftMotorTwo, rollerMotor;
    public static DigitalInput liftInput, intakeInput;
    
    public Intake(){
    	liftMotorOne = new CANTalon(33);
    	liftMotorTwo = new CANTalon(44);
    	rollerMotor = new CANTalon(55);
    }
    
    public void liftControl(double speed){
    	liftMotorOne.set(-speed);
    	liftMotorTwo.set(speed);
    }
    
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

