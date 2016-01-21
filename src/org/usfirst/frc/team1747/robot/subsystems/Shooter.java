package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem{
	
	CANTalon leftShooter;
	CANTalon rightShooter;
	
	public Shooter(){
		System.out.println("Shooter created");
		leftShooter = new CANTalon(1);
		rightShooter = new CANTalon(22);
	}
	
	public void shoot(double speed){
		System.out.println("Shooting" + speed);
		leftShooter.set(-speed);
		rightShooter.set(speed);
	}
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
