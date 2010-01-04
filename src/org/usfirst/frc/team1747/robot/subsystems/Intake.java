package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    CANTalon liftCIM1, liftCIM2, rollerCIM;
    
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
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

