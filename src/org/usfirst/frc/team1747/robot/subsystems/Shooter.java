package org.usfirst.frc.team1747.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem{
	
	CANTalon leftShooter1, leftShooter2;
	CANTalon rightShooter1, rightShooter2;
	
	public Shooter(){
		System.out.println("Shooter created");
		leftShooter1 = new CANTalon(1);
		rightShooter1 = new CANTalon(22);
		leftShooter2 = new CANTalon(11);
		rightShooter2 = new CANTalon(2);
		
	}
	
	public void shoot(double speed){
		System.out.println("Shooting" + speed);
		leftShooter1.set(-speed);
		leftShooter2.set(-speed);
		rightShooter1.set(speed);
		rightShooter2.set(speed);
	}
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
