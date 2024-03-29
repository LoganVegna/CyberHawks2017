package org.usfirst.frc.team2875.robot.subsystems;

import org.usfirst.frc.team2875.robot.Debug;
import org.usfirst.frc.team2875.robot.Gearbox;
import org.usfirst.frc.team2875.robot.IO;
import org.usfirst.frc.team2875.robot.Robot;
import org.usfirst.frc.team2875.robot.commands.Drive;

import auto.AutoMid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem{
	
	private boolean drivingStraight;
	private double straight;
	private int count;
	public int straightdrive_delay = 4;
	public Drivetrain(){
		System.out.println("starting");
		
		//1/12 -- 0 is left, 1 is right TBU

	}
	public void setSpeed(double speed){
		Robot.leftGearbox.setSpeed(speed);
		Robot.rightGearbox.setSpeed(speed);
	}
	/*public void setSpeed(int num, double speed){
		gearbox[num].setSpeed(speed);
	}*/
	private double getSign(double in){
		if(in == 0){
			return 0;
		}
		return (in / Math.abs(in));
	}
	public void input(double forward, double left, double right){
		left /= 2;
		right /= 2;
		//drive(forward,left,right);
		
		if (Math.abs(forward)-IO.JOY_DEADZONE > 0 && left - IO.TRIGGER_DEADZONE<= 0 && right - IO.TRIGGER_DEADZONE<=0){
			
			if (count >0){
				straight = Robot.gyroscope.get_heading();
				count--;
				drive(-forward,left,right);
			}else{
				straightDrive(forward);
			}
		}else if(left+ right == 0){
			count = 0;
			straight = Robot.gyroscope.get_heading();
			drive(-forward, left, right);
		}else if (Math.abs(forward)-IO.JOY_DEADZONE > 0){
			drive(-forward, left, right);
			count = straightdrive_delay;
		}else{
			drive(-forward, left, right);
			count = 0;
		}
	}
	public void straightDrive(double iforward){
		double forward = -iforward;
		Double cur = Robot.gyroscope.get_heading() - straight;
		Debug.log("Straight", straight);
		
			if (cur> 0){
				Robot.leftGearbox.setSpeed((forward-Math.abs(cur)/90));
				Robot.rightGearbox.setSpeed((forward+Math.abs(cur)/90));
			}else if(cur <0 ){
				Robot.rightGearbox.setSpeed((forward-Math.abs(cur)/90));
				Robot.leftGearbox.setSpeed((forward+Math.abs(cur)/90));
			}else{
				Robot.rightGearbox.setSpeed(forward);
				Robot.leftGearbox.setSpeed(forward);
			}
		
		
	}
	public void drive(double forward, double left, double right){
		//System.out.println(Robot.vis.getAngle());
		double g1 = forward + ((right) - (left));
		double g2 = -forward + ((right) - (left));
		double speed2 = Math.max(0, (Math.abs(g1)-IO.JOY_DEADZONE)) * getSign(g1);
		double speed1 = Math.max(0, (Math.abs(g2)-IO.JOY_DEADZONE)) * getSign(g2);
		;	
		
		Robot.rightGearbox.setSpeed(-speed1);
		Robot.leftGearbox.setSpeed(speed2);	
	}
	protected void initDefaultCommand() {
		setDefaultCommand(new Drive());
	}
	public void stop() {
		
		Robot.rightGearbox.stop();
		Robot.leftGearbox.stop();
	}
	

}
