package org.usfirst.frc.team1747.robot;

import java.util.concurrent.Executors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyCamera implements Runnable, SDLogger {
	private final static int MAX_BYTES = 64;
	// The Pixy Camera outputs the following shape centroid values:
	// X centroid: Ranges from 0 to 255 (0 is the leftmost pixel from the
	// perspective of the camera lens)
	// Y centroid: ranges from 0 to 199 (0 is the topmost pixel)
	// NOTE: that 0 is NOT the center
	// The center is (128, 100)
	//
	// TODO: Verify
	// final private static double TOP_LEFT_X = 158, TOP_LEFT_Y = 106.5,
	// BOTTOM_RIGHT_X = 166, BOTTOM_RIGHT_Y = 123; // Original values
	final private static double TOP_LEFT_X = 160, TOP_LEFT_Y = 80, BOTTOM_RIGHT_X = 180, BOTTOM_RIGHT_Y = 90;
	/*
	 * Converted original values from previous line to the proper scaling as
	 * specified in comments above
	 */
	private I2C pixyCamera;
	private PixyData pixyData;

	public PixyCamera() {
		// TODO: Verify
		pixyCamera = new I2C(I2C.Port.kOnboard, 0x56);
		Executors.newSingleThreadExecutor().submit(this);
	}

	private char parseByteData(byte[] data, int lowIndex, int highIndex) {
		int temp = 0b00000000;
		temp = (((data[highIndex] & 0xff) << 8) | (data[lowIndex] & 0xff));
		return (char) (temp);
	}

	@Override
	public void run() {
		// noinspection InfiniteLoopStatement
		while (true) {
			byte[] pixyValues = new byte[MAX_BYTES];
			if (!pixyCamera.readOnly(pixyValues, MAX_BYTES)) {
				int i = 0;
				// TODO: Verify this logic in case of overflow
				while (!(((pixyValues[i] & 0xFF) == 0x55) && (pixyValues[i + 1] & 0xff) == 0xAA) && i < 50) {
					i++;
				}
				if (i > 50) {
					i = 49;
				}
				while (!(((pixyValues[i] & 0xFF) == 0x55) && (pixyValues[i + 1] & 0xff) == 0xAA) && i < 50) {
					i++;
				}
				int x = parseByteData(pixyValues, i + 8, i + 9);
				int y = parseByteData(pixyValues, i + 10, i + 11);
				// System.out.println("X: " + x);
				pixyData = new PixyData(x, y);
			} else {
				pixyData = null;
				System.out.println("No Pixy Data.");
			}
		}
	}

	public boolean shouldMoveForward() {
		if (pixyData.getY() > BOTTOM_RIGHT_Y)
			System.out.println("Forward");
		return pixyData.getY() > BOTTOM_RIGHT_Y;
	}

	public boolean shouldMoveBackward() {
		if (pixyData.getY() < TOP_LEFT_Y)
			System.out.println("Back");
		return pixyData.getY() < TOP_LEFT_Y;
	}

	public boolean shouldTurnRight() {
		if (pixyData.getX() > BOTTOM_RIGHT_X) {
			System.out.println("Right");
		}
		return pixyData.getX() > BOTTOM_RIGHT_X;
	}

	public boolean shouldTurnLeft() {
		if (pixyData.getX() < TOP_LEFT_X)
			System.out.println("Left");
		return pixyData.getX() < TOP_LEFT_X;
	}

	public boolean isOutOfRange() {
		return pixyData != null
				&& !(pixyData.getX() <= 0 || pixyData.getX() > 255 || pixyData.getY() <= 0 || pixyData.getY() > 199);
	}

	public boolean shouldShoot() {
		System.out.println("Shoot");
		return !(shouldMoveForward() || shouldMoveBackward() || shouldTurnLeft() || shouldTurnRight());
	}

	private class PixyData {
		private int x;
		private int y;

		public PixyData(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	@Override
	public void logToSmartDashboard() {
		if (isOutOfRange()) {
			SmartDashboard.putNumber("Pixy X", pixyData.getX());
			SmartDashboard.putNumber("Pixy Y", pixyData.getY());
		}
	}
}
