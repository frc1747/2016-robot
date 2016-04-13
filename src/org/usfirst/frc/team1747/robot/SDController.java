package org.usfirst.frc.team1747.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDController {

	private SendableChooser autonPosition;
	private SendableChooser defenseType;
	private NetworkTable networkTable = NetworkTable.getTable("imageProcessing");
	private List<SDLogger> sdLoggers;

	public SDController() {
		SmartDashboard.putData(Scheduler.getInstance());
		SmartDashboard.putBoolean("LastSecondShot", false);
		autonPosition = new SendableChooser();
		autonPosition.addObject("Don't shoot", Positions.NOTHING);
		autonPosition.addObject("1", Positions.ONE);
		autonPosition.addObject("2", Positions.TWO);
		autonPosition.addDefault("3", Positions.THREE);
		autonPosition.addObject("4", Positions.FOUR);
		autonPosition.addObject("5", Positions.FIVE);
		SmartDashboard.putData("Auton Position", autonPosition);
		defenseType = new SendableChooser();
		defenseType.addObject("Portcullis", Defense.PORTICULLIS);
		defenseType.addObject("CHEVAL DE FRISE", Defense.CHEVAL_DE_FRISE);
		defenseType.addObject("Ramparts", Defense.RAMPARTS);
		defenseType.addDefault("Moat", Defense.MOAT);
		defenseType.addObject("Drawbridge", Defense.DRAWBRIDGE);
		defenseType.addObject("Sally Port", Defense.SALLY_PORT);
		defenseType.addObject("Rock Wall", Defense.ROCK_WALL);
		defenseType.addObject("Rough Terrain", Defense.ROUGH_TERRAIN);
		defenseType.addObject("Low Bar", Defense.LOW_BAR);
		SmartDashboard.putData("Defense", defenseType);
		sdLoggers = new ArrayList<>();
	}

	public void addSystem(SDLogger system) {
		sdLoggers.add(system);
	}

	public void addSystems(SDLogger... systems) {
		for (SDLogger system : systems) {
			addSystem(system);
		}
	}

	public void refresh() {
		sdLoggers.forEach(SDLogger::logToSmartDashboard);
		SmartDashboard.putString("ShooterDirection", networkTable.getString("ShootDirection", "robotUnknown"));
		SmartDashboard.putNumber("ShooterRads", networkTable.getNumber("ShootRads", 0.0));
	}

	public Positions getAutonPosition() {
		return (Positions) autonPosition.getSelected();
	}

	public Defense getDefenseType() {
		return (Defense) defenseType.getSelected();
	}

	public enum Positions {
		NOTHING, ONE, TWO, THREE, FOUR, FIVE
	}

	public enum Defense {
		PORTICULLIS, CHEVAL_DE_FRISE, RAMPARTS, MOAT, DRAWBRIDGE, SALLY_PORT, ROCK_WALL, ROUGH_TERRAIN, LOW_BAR
	}
}