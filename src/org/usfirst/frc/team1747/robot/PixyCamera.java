package org.usfirst.frc.team1747.robot;

import edu.wpi.first.wpilibj.I2C;

import java.util.concurrent.Executors;

public class PixyCamera implements Runnable {
	private final static int MAX_BYTES = 64;
	// The Pixy Camera outputs the following shape centroid values:
	//     X centroid: Ranges from 0 to 255       (0 is the leftmost pixel from the perspective of the camera lens)
	//	   Y centroid: ranges from 0 to 199       (0 is the topmost pixel)
	// NOTE: that 0 is NOT the center
	// The center is (128, 100)
	//
	//TODO: Verify
	final private static double TOP_LEFT_X = 149.5, TOP_LEFT_Y = 117, BOTTOM_RIGHT_X = 164.5, BOTTOM_RIGHT_Y = 139; // <-- these need to be changed
	private I2C pixyCamera;
	private PixyData pixyData;


	public PixyCamera() {
		//TODO: Verify
		pixyCamera = new I2C(I2C.Port.kOnboard, 0x54);
		Executors.newSingleThreadExecutor().submit(this);
	}

	private char parseByteData(byte[] data, int lowIndex, int highIndex) {
		return (char) (((data[highIndex] & 0xff) << 8) | (data[lowIndex] & 0xff));
	}

	@Override
	public void run() {
		//noinspection InfiniteLoopStatement
		while (true) {
			byte[] pixyValues = new byte[MAX_BYTES];
			if (!pixyCamera.readOnly(pixyValues, MAX_BYTES)) {
				int i = 0;
				//TODO: Verify this logic in case of overflow
				while (!(((pixyValues[i] & 0xFF) == 0x55) && (pixyValues[i + 1] & 0xff) == 0xAA) && i < 50) {
					i++;
				}
				if (i > 50) {
					i = 49;
				}
				while (!(((pixyValues[i] & 0xFF) == 0x55) && (pixyValues[i + 1] & 0xff) == 0xAA) && i < 50) {
					i++;
				}
				int x = parseByteData(pixyValues, i + 6, i + 7);
				int y = parseByteData(pixyValues, i + 8, i + 9);
				int width = parseByteData(pixyValues, i + 10, i + 11);
				int height = parseByteData(pixyValues, i + 12, i + 13);
				pixyData = new PixyData(x, y, width, height);
			} else {
				pixyData = null;
			}
		}
	}

	public boolean shouldMoveForward() {
		return pixyData.getY() > BOTTOM_RIGHT_Y;
	}

	public boolean shouldMoveBackward() {
		return pixyData.getY() < TOP_LEFT_Y;
	}

	public boolean shouldTurnLeft() {
		return pixyData.getX() < TOP_LEFT_X;
	}

	public boolean shouldTurnRight() {
		return pixyData.getX() > BOTTOM_RIGHT_X;
	}

	public boolean shouldShoot() {
		return !(shouldMoveForward() || shouldMoveBackward() || shouldTurnLeft() || shouldTurnRight());
	}

	private class PixyData {
		private int x;
		private int y;
		private int width;
		private int height;

		public PixyData(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}
}
