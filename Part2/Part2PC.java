
/*
 * September 21, 2009
 * Author Tawat Atigarbodee
 *
 * This program creates a Control Window for controlling NXT brick running NXTtr.java via USB.
 *
 * To compile this program.
 *  -   Install Lejos 0.8.5
 *  -   Include Lejos_nxj library to the project path
 *  -   Compile the program with javac (I use Eclipse)
 *
 * To use this program
 *  -   At NXT brick, run NXTtr.java
 *  -   Run NXTremoteControl_TA
 *  -   **Click “Connect” button first**
 *  -   Control the robot by using buttons or keyboard
 *       a, w, s, d for direction
 *       i for speed up and  k for slow down
 *
 * Note: This program is a partial of my project file.
 * I use “USBSend” and “USBReceive” created by Lawrie Griffiths
 * as a pattern for creating USB communication between PC and NXT.
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.*;
import lejos.pc.comm.NXTConnector;
import lejos.util.Delay;

class PointValue {
	public double x;
	public double y;
	public int light;
	
	PointValue(double xinit, double yinit, int lightinit) 
	{
		this.x = xinit;
		this.y = yinit;
		this.light = lightinit;
	}
}

public class Part2PC {
	private enum Mode {
		Record, Pause, Test
	}
	public Mode mode = Mode.Record;
	public static TrackerReader tracker;
	public static DataOutputStream outData;
	public static NXTConnector link;
	public static DifferentialPilot pilot;
	public static LightSensor light;
	public ArrayList<PointValue> pointVals;

	public Part2PC() {
		pointVals = new ArrayList<PointValue>();
		pilot = new DifferentialPilot(5.5f, 5.5f, 
							Motor.A, Motor.C);
		tracker = new TrackerReader();
		light = new LightSensor(SensorPort.S2, true);
		pilot.setTravelSpeed(1);
		tracker.start();
		
		while (true) 
		{
			System.out.println(this.mode.toString());
			switch (mode)
			{
				case Record:
					recordDataPoint();
					break;
				case Pause:
					
					break;
				case Test:
					
				break; 
				default:
					
			}
		}
	}
	
	public void recordDataPoint() {
		// move the robot into a new position for recording a data point
		Delay.msDelay(500);
		pilot.travel(0.5);
		
		/* possible tracker values: x, y, a, theta, targetx, targety */
		double x = tracker.x;
		double y = tracker.y;
		double theta = tracker.theta;
		double targetx = tracker.targetx;
		double targety = tracker.targety;
		int l = light.getLightValue();
		
		// record the data point
		System.out.println("x = " + tracker.x + 
						  " y = " + tracker.y + " light = " + l);
		pointVals.add(new PointValue(x, y, l));
	}

	public void saveData() {
		
	}
    
	public static void main(String[] args) 
	{
		Part2PC pc = new Part2PC();
	}

}
