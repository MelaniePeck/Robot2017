package org.usfirst.frc.team2506.robot;


import java.math.*;

public class SwerveDrive {
	public SwerveDrive () {
		
	}
	
	public final double L = 18;
	public final double W = 14;
	
	private int clock = 0;
	
	WheelDrive frontRight;
	WheelDrive backRight;
	WheelDrive frontLeft;
	WheelDrive backLeft;
	
	public SwerveDrive (WheelDrive frontRight, WheelDrive backRight, WheelDrive frontLeft, WheelDrive backLeft) {
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
	}
	
	public void drive (double y1, double x1, double x2) {
		clock++;
		y1 *= -1;
		double R = Math.sqrt(L * L + W * W);
		double a = x1 - x2 * (L / R);
		double b = x1 + x2 * (L / R);
		double c = y1 - x2 * (W / R);
		double d = y1 + x2 * (W / R);

		double ws1 = Math.sqrt ((b * b) + (c * c));
		double ws2 = Math.sqrt ((b * b) + (d * d));
		double ws3 = Math.sqrt ((a * a) + (d * d));
		double ws4 = Math.sqrt ((a * a) + (c * c));
		
		double wa1 = Math.atan2 (b, c) / Math.PI;
		double wa2 = Math.atan2 (b, d) / Math.PI;
		double wa3 = Math.atan2 (a, d) / Math.PI;
		double wa4 = Math.atan2 (a, c) / Math.PI;
		
		frontRight.drive (wa2, ws1);
		frontLeft.drive (wa1, ws2);
		backRight.drive (wa3, -ws3);
		backLeft.drive (wa4, -ws4);
	}
}