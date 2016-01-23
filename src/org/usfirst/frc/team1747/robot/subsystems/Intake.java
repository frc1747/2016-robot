package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    CANTalon liftCIM1, liftCIM2, rollerCIM;
    public static DigitalInput liftInput, intakeInput;
    
    public Intake(){
    	liftCIM1 = new CANTalon(33);
    	liftCIM2 = new CANTalon(44);
    	rollerCIM = new CANTalon(55);
    	
    }
    
    public void liftControl(double speed){
    	liftCIM1.set(-speed);
    	liftCIM2.set(speed);
    	
    }
    
    public void rollerControl(double speed){
    	rollerCIM.set(speed);
    	
    }
    
    public DigitalInput getLiftInput(){
    	return liftInput;
    }
    
    public DigitalInput getIntakeInput(){
    	return intakeInput;
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

