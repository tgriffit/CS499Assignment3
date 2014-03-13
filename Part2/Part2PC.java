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

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.*;
import lejos.pc.comm.NXTConnector;
import lejos.util.Delay;

class PointValue {
	public double x;
	public double y;
	public int light;

	PointValue(double xinit, double yinit, int lightinit) {
		this.x = xinit;
		this.y = yinit;
		this.light = lightinit;
	}

	public String getHeader() {
		return "@RELATION\tHeightMapping\n\n" + "@ATTRIBUTE\tx\tNUMERIC\n"
				+ "@ATTRIBUTE\ty\tNUMERIC\n" + "@ATTRIBUTE\tlight\tNUMERIC\n\n";
	}

	public String toString() {
		return x + "," + y + "," + light + "\n";
	}
}

public class Part2PC {
	public UIHandler.Mode mode = UIHandler.Mode.Record;
	public static TrackerReader tracker;
	public static DataOutputStream outData;
	public static NXTConnector link;
	public static DifferentialPilot pilot;
	public static LightSensor light;
	public static UIHandler ui;
	public ArrayList<PointValue> pointVals;

	public Part2PC() {
		pointVals = new ArrayList<PointValue>();
		ui = new UIHandler(this);
		pilot = new DifferentialPilot(5.5f, 5.5f, Motor.A, Motor.C);
		tracker = new TrackerReader();
		light = new LightSensor(SensorPort.S2, true);
		pilot.setTravelSpeed(1);
		tracker.start();

		System.out
				.println("Please run 'python tracker.py' and select the colour");

		// wait until the tracker has made a connection to the camera
		while (!tracker.hasConnection)
			Delay.msDelay(500);

		while (true) {
			mode = ui.getMode();
			switch (mode) {
			case Record:
				recordDataPoint();
				break;
			case Pause:
				showTestData();
				break;
			case Test:

				break;
			default:
			}
		}
	}
	
	// print the data that is being received from tracker to syso
	public void showTestData() {
		double x = tracker.x;
		double y = tracker.y;
		double theta = tracker.theta;
		double targetx = tracker.targetx;
		double targety = tracker.targety;
		int l = light.getLightValue();
		
		System.out.println("x: " + x + " y: " + y + " theta: " + theta);
		System.out.println("trackerx: " + targetx + " trackery: " + targety);
		System.out.println("light: " + l);
	}
	
	public void recordDataPoint() {
		/* possible tracker values: x, y, a, theta, targetx, targety */
		double x = tracker.x;
		double y = tracker.y;
		double theta = tracker.theta;
		double targetx = tracker.targetx;
		double targety = tracker.targety;
		int l = light.getLightValue();

		// dont record if data == 0.0 because no tracker.py colour has been
		// selected
		if (x == 0.0 && y == 0.0 && theta == 0.0 && targetx == 0.0
				&& targety == 0.0)
			return;

		// record the data point
		System.out.println("x = " + tracker.x + " y = " + tracker.y
				+ " light = " + l);
		pointVals.add(new PointValue(x, y, l));
		System.out.println("Num data pts: " + pointVals.size());
		// move the robot into a new position for next data recording call
		Delay.msDelay(500);
		pilot.travel(0.5);
	}

	public void saveData() {
		writeArffFile(pointVals);
	}

	private void writeArffFile(ArrayList<PointValue> results) {
		String filename = "p2output-" + System.currentTimeMillis() + ".arff";

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));

			// Header!
			out.write(results.get(0).getHeader());

			// Data!
			out.write("@DATA\n");
			for (int i = 0; i < results.size(); ++i) {
				out.write(results.get(i).toString());
			}

			out.close();
		} catch (IOException e) {

		}

		System.out.println("Saved as " + filename + "\n");
	}

	public static void main(String[] args) {
		Part2PC pc = new Part2PC();
	}

}
