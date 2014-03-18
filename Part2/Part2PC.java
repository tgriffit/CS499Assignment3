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

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
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
	public String arfffile = null;
	private WekaHandler weka = null;
	public ArrayList<PointValue> pointVals;

	public Part2PC() {
		pointVals = new ArrayList<PointValue>();
		ui = new UIHandler(this);
		pilot = new DifferentialPilot(56, 116, Motor.C, Motor.A);
		tracker = new TrackerReader();
		light = new LightSensor(SensorPort.S2, true);
		pilot.setTravelSpeed(2);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
		tracker.start();

		System.out
				.println("Please run 'python tracker.py' and select the colour");

		// wait until the tracker has made a connection to the project
		// while (!tracker.hasConnection)
		// Delay.msDelay(500);

		while (true) {
			mode = ui.getMode();
			switch (mode) {
			case Record:
				recordDataPoint();
				break;
			case Pause:

				break;
			case Test:
				if (weka == null) {
					arfffile = ui.getArffname();
					weka = new WekaHandler(arfffile);
				}
				showTestData();
				break;
			case Demo:
				// get two corners from tracker.py
				if (weka == null) {
					arfffile = ui.getArffname();
					weka = new WekaHandler(arfffile);
				}
				Point topleft = new Point(125, 80, 100);
				Point topright = new Point(500, 345, 100);
				Point start = new Point(450, 300, 100);
				Point end = new Point(140, 190, 100);
				// arbitrary light vals
				ArrayList<Cluster> clusters = weka.getClusters(125, 80, 500,
						345);
				PathFinding p = new PathFinding(clusters);
				ArrayList<Point> path = p.findPath(start, end);
				ui.drawPath(path, clusters, topleft, topright);
				System.out.println(path);
				ui.mode = UIHandler.Mode.Pause;
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
		int l = getQuantizedLight();

		System.out.println("x: " + x + " y: " + y + " theta: " + theta);
		System.out.println("trackerx: " + targetx + " trackery: " + targety);
		System.out.println("light: " + l);
		// int cluster = weka.getClusterNum(x, y);
		// System.out.println("Cluster num: " + cluster);
	}

	public void recordDataPoint() {
		/* possible tracker values: x, y, a, theta, targetx, targety */
		double x = tracker.x;
		double y = tracker.y;
		double theta = tracker.theta;
		double targetx = tracker.targetx;
		double targety = tracker.targety;
		int l = getQuantizedLight();

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
		Delay.msDelay(100);
		pilot.travel(0.8);
	}

	public void saveData() {
		writeArffFile(pointVals);
	}

	// gives a wider gap between the different light values to improve
	// clustering
	private int getQuantizedLight() {
		int l = light.getLightValue();

		if (l <= 45 && l >= 43)
			return 100;
		else if (l <= 42 && l >= 40)
			return 80;
		else if (l <= 39 && l >= 35)
			return 60;
		else if (l <= 34 && l >= 31)
			return 40;
		else if (l <= 30 && l >= 28)
			return 20;

		return 0;
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
