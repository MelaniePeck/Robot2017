package org.usfirst.frc.team2506.robot;

import org.usfirst.frc.team2506.robot.XboxController.JoystickManager;
import org.usfirst.frc.team2506.robot.XboxController.XboxButtons;
import org.usfirst.frc.team2506.robot.XboxController.Events.IButtonPressed;
import org.usfirst.frc.team2506.robot.XboxHandlers.ButtonStartHandler;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Joystick joystick = new Joystick (0);
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		gyro.calibrate();
		gyro.setPIDSourceType(PIDSourceType.kDisplacement);
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {

	}
	private JoystickManager joystickManager = new JoystickManager (joystick);
	private IButtonPressed startButtonPressedHandler = new ButtonStartHandler (this);
	public void teleopInit() {
		swerveDrive.drive(0, 0, 0);
		joystickManager.addButtonPressedListener(XboxButtons.BUTTON_A, startButtonPressedHandler);
		joystickManager.start();

		gyro.calibrate();
	}
	
	WheelDrive backLeft = new WheelDrive(7, 6, 3, 24);
	WheelDrive frontLeft = new WheelDrive(5, 4, 2, -25);
	WheelDrive backRight = new WheelDrive(2, 3, 1, -35);
	WheelDrive frontRight = new WheelDrive(0, 1, 0, -101);
	
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	private SwerveDrive swerveDrive = new SwerveDrive (frontRight, backRight, frontLeft, backLeft);
	private TankDrive tankDrive = new TankDrive (backLeft, backRight, frontLeft, backRight);
	public boolean isSwerveDrive = true;
	
	@Override
	public void teleopPeriodic() {
		double yAxis = joystick.getRawAxis(0);
		double xAxis = joystick.getRawAxis(1);
		double rotation = joystick.getRawAxis(4);
		double gyroAngle = gyro.getAngle() % 360;
		double[] rotatedInputs = rotateInputs(xAxis, yAxis, gyroAngle);
		if (yAxis > 0.1 || yAxis < -0.1 || xAxis > 0.1 || xAxis < -0.1 || rotation > 0.1 || rotation < -0.1) {
			swerveDrive.drive (squareAxis(rotatedInputs [0]) * 0.7, -squareAxis(rotatedInputs [1]) * 0.7, -squareAxis(rotation) * 0.5);
		} else { /*
			backLeft.coast ();
			backRight.coast ();
			frontLeft.coast ();
			frontRight.coast (); */
		}
	}
	
	double squareAxis(double axis) {
		return axis * axis * Math.signum(axis);
	}
	
	public static double[] rotateInputs(double x, double y, double gyroAngle) {
		double[] retVal = new double[2];
		double theta = Math.toRadians(gyroAngle);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);

		// Rotated X input
		retVal[0] = (x * cosTheta) - (y * sinTheta);
		// Rotated Y input
		retVal[1] = (x * sinTheta) + (y * cosTheta);

		return retVal;
	}
	
	WheelDrive[] wheels = { frontRight, frontLeft, backRight, backLeft };
	WheelDrive testWheel = wheels[0];
	boolean b8 = false;
	@Override
	public void testPeriodic()
	{
		swerveDrive.drive(0, 0, 0);
		
		for (int b = 1; b <= 4; b++)
		{
			if (joystick.getRawButton(b))
			{
				testWheel = wheels[b-1];
				System.out.println("calibrating wheel " + b);
			}
		}
		
		double stick = joystick.getRawAxis(1);
		
		if (stick > .2) testWheel.incrementOffset();
		else if (stick < -0.2) testWheel.decrementOffset();
		
		if (joystick.getRawButton(8))
		{
			if (!b8)
			{
				System.out.println("front right " + frontRight.getOffset());
				System.out.println("front left " + frontLeft.getOffset());
				System.out.println("back right " + backRight.getOffset());
				System.out.println("back left " + backLeft.getOffset());
			}
			b8 = true;
		}
		else
		{
			b8 = false;
		}
	}
}

