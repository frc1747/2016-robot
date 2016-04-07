package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1747.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1747.robot.subsystems.Intake;
import org.usfirst.frc.team1747.robot.subsystems.Scooper;
import org.usfirst.frc.team1747.robot.subsystems.Shooter;

public class SDController {

    private SendableChooser autonPosition;
    private SendableChooser defenseType;
    private DriveTrain driveTrain;
    private Shooter shooter;
    private Intake intake;
    private Scooper scooper;
    private NetworkTable networkTable = NetworkTable.getTable("imageProcessing");

    public SDController() {
        driveTrain = Robot.getDriveTrain();
        shooter = Robot.getShooter();
        intake = Robot.getIntake();
        scooper = Robot.getScooper();
        SmartDashboard.putData(Scheduler.getInstance());
        autonPosition = new SendableChooser();
        autonPosition.addObject("Don't shoot", Positions.NOTHING);
        autonPosition.addObject("1", Positions.ONE);
        autonPosition.addObject("2", Positions.TWO);
        autonPosition.addDefault("3", Positions.THREE);
        autonPosition.addObject("4", Positions.FOUR);
        autonPosition.addObject("5", Positions.FIVE);
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
        SmartDashboard.putData("Auton Position", autonPosition);
    }

    public void refresh() {
        // oi.getController().logToSmartDashboard();
        driveTrain.logToSmartDashboard();
        shooter.logToSmartDashboard();
        intake.logToSmartDashboard();
        scooper.logToSmartDashboard();
        SmartDashboard.putBoolean("LastSecondShot", false);
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