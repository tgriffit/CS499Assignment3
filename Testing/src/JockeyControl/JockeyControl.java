package JockeyControl;

import java.awt.Color;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import referees.OnePlayerReferee;
import agents.LoneAgent;
import algorithms.WatkinsSelector;
import ReinforcementLearning.*;
import lejos.nxt.*;
import lejos.util.Delay;

class Result {
	MovementType action;

	// The full header for arff files
	String getHeader() {
		return "OH NOES THIS IS THE BASE CLASS. ABORT!";
	}
}

class Result1 extends Result {
	int pedDist; // Don't want to hit them pedestrians

	String getHeader() {
		return "@RELATION\tPedestrianAvoidance\n\n" + "@ATTRIBUTE\tpedDist\tNUMERIC\n"
				+ "\n\n";
	}

	public String toString() {
		return pedDist + "\n";
	}
}

public class JockeyControl extends JFrame {

	private static WekaHandler weka;
	
	// Reinforcement learning components
	private static Environment environment;
	private static LoneAgent agent;
	private static WatkinsSelector sel;
	private static OnePlayerReferee ref;
	private static double epsilon = 0.30;
	static final String agentFile = "AgentResults";

	private enum Mode {
		Stop, Wait, Part1EM, Part1KM, Part2, Part3, Part4
	}

	private static Mode mode = Mode.Wait;
	private static boolean dataCollection = false;

	private static MovementType movementMode = MovementType.Stop;

	private static ArrayList<Result> results = new ArrayList<Result>();

	static MotorPort leftMotor = MotorPort.C;
	static MotorPort rightMotor = MotorPort.A;

	static LightSensor lightSensor = new LightSensor(SensorPort.S2);
	static UltrasonicSensor rightUltrasound = new UltrasonicSensor(SensorPort.S3);
	static UltrasonicSensor frontUltrasound = new UltrasonicSensor(SensorPort.S4);
	static UltrasonicSensor backUltrasound = new UltrasonicSensor(SensorPort.S1);

	public static JockeyControl NXTrc;

	public static JLabel modeLbl;
	public static JLabel commands;
	public static ButtonHandler bh = new ButtonHandler();

	public JockeyControl() {
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		setTitle("Jockey Self-Control");
		setBounds(400, 350, 300, 200);
		addMouseListener(bh);
		addKeyListener(bh);

		modeLbl = new JLabel();
		modeLbl.setForeground(Color.BLUE);
		setCollectionHeader();
		p.add(modeLbl);

		String cmds = "<html>Buttons:<br>" + "1: Part 1<br>" + "2: Part 2<br>"
				+ "3: Part 3<br>" + "<br>"
				+ "c: Record Data/Stop Recording Data<br>" + "<br>"
				+ "s: Stop<br>" + "<br>" + "q: Quit</html>";

		commands = new JLabel(cmds);
		p.add(commands);

		add(p);
	}

	public static void main(String[] args) {
		NXTrc = new JockeyControl();
		NXTrc.setVisible(true);
		NXTrc.requestFocusInWindow();
		NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (mode != Mode.Stop) {
			switch (mode) {
			case Part1EM:
				if (dataCollection) {
					recordDataPart1();
					move(movementMode, MovementType.getPower(movementMode));
				} else {
					int result = doWekaPart1EM();
					MovementType r = MovementType.intToMovementType(result);
					move(r, MovementType.getPower(r));
				}
				break;
			case Part1KM:
				if (dataCollection) {
					recordDataPart1();
					move(movementMode, MovementType.getPower(movementMode));
				} else {
					int result = doWekaPart1KMeans();
					MovementType r = MovementType.intToMovementType(result);
					move(r, MovementType.getPower(r));
				}
				break;
			case Part3:
			case Part4:
				doReinforcementLearning();
				mode = Mode.Wait;
				break;
			default:
				stahp();
				break;
			}
		}

		stahp();
		lightSensor.setFloodlight(false);

		System.exit(0);
	}

	private static void recordDataPart1() {
		MovementType m = movementMode;	// Cache it to avoid having movement mode change during data collection
		if (m != MovementType.Stop) {
			Result1 r = new Result1();
			r.pedDist = rightUltrasound.getDistance();
			r.action = m;
			results.add(r);
		}
	}

	private static int doWekaPart1EM() {
		return weka.getEMCluster(rightUltrasound.getDistance());
	}
	
	private static int doWekaPart1KMeans() {
		return weka.getKmeansCluster(rightUltrasound.getDistance());
	}

	private static void doReinforcementLearning() {
		System.out.println("Starting episode");
		
		ref.episode(environment.getCurrentState());
		
		System.out.println("Episode Reward: " + ref.getRewardForEpisode());
		epsilon *= 0.99;
		sel.setEpsilon(epsilon);
	}
	
	// Tries to go straight left
	public static void squiggleLeft() {
		int power = 20;
		int delay = 250;
		move(MovementType.TurnLeft, power);
		Delay.msDelay(delay);
		move(MovementType.ForwardSlow, power);
		Delay.msDelay(delay);
//		move(MovementType.TurnRight, power);
//		Delay.msDelay(delay);
		stahp();
		Delay.msDelay(10);
		straighten();
		move(MovementType.Backward, power * 3 / 4);
		Delay.msDelay(delay);

		stahp();
		Delay.msDelay(500);
	}
	
	// Tries to go straight right
	public static void squiggleRight() {
		int power = 20;
		int delay = 250;
		move(MovementType.TurnRight, power);
		Delay.msDelay(delay);
		move(MovementType.ForwardSlow, power);
		Delay.msDelay(delay);
		//move(MovementType.TurnLeft, power);
		//Delay.msDelay(delay);
		stahp();
		Delay.msDelay(10);
		straighten();
		move(MovementType.Backward, power * 3 / 4);
		Delay.msDelay(delay);
		
		stahp();
		Delay.msDelay(500);
	}
	
	public static void turnLeft() {
		int power = 20;
		int delay = 20;
		move(MovementType.TurnLeft, power);
		Delay.msDelay(delay);

		stahp();
		Delay.msDelay(100);
	}
	
	public static void turnRight() {
		int power = 20;
		int delay = 20;
		move(MovementType.TurnRight, power);
		Delay.msDelay(delay);

		stahp();
		Delay.msDelay(100);
	}
	
	public static void go() {
		int power = mode == Mode.Part4 ? MovementType.getPower(MovementType.intToMovementType(doWekaPart1EM()))*2 : 30;
		int delay = 250;
		move(MovementType.ForwardSlow, power);
		Delay.msDelay(delay);
		stahp();
	}
	
	public static void straighten() {
		int front = getFrontDistance();
		int back = getBackDistance();
		
		while (Math.abs(front - back) > 2) {
			if (front > back) {
				turnLeft();
			}
			else {
				turnRight();
			}
			
			front = getFrontDistance();
			back = getBackDistance();
		}
	}
	
	public static int getFrontDistance() {
		return frontUltrasound.getDistance();
	}
	
	public static int getBackDistance() {
		return backUltrasound.getDistance();
	}
	
	public static int getLightValue() {
		return lightSensor.getLightValue();
	}

	private static void writeArffFile(ArrayList<Result> results) {
		String filename = "output - " + System.currentTimeMillis() + ".arff";
		
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

	private static boolean resetWeka() {
		final JFileChooser fc = new JFileChooser();
		String arfffile = "";

		try {
			if (fc.showOpenDialog(NXTrc) == JFileChooser.APPROVE_OPTION) {
				arfffile = fc.getSelectedFile().getPath();
			} else {
				return false;
			}

			weka = new WekaHandler(arfffile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private static void setupRL() {
		if (agent != null) {
			return;
		}
		
		environment = new Environment();
		
		File file = new File(agentFile + ".agt");
		if (file.exists()) {
			agent = (LoneAgent) LoneAgent.readAgent(agentFile, environment);
			sel = (WatkinsSelector) agent.getAlgorithm();
		}
		else {
			System.out.println("Creating a new agent.");
			
			sel = new WatkinsSelector(0.7);
			
			sel.setEpsilon(epsilon);
			sel.setGeometricAlphaDecay(); 
			sel.setAlpha(0.3); 
			
			agent = new LoneAgent(environment, sel);
		}
		
		if (!dataCollection) {
			// If we're in doing stuff mode then we want to test what we've learned
			agent.freezeLearning();
			epsilon = 0;
			sel.setEpsilon(epsilon);
		}
		
		ref = new OnePlayerReferee(agent);
	}

	private static void driveForward(int power) {
		leftMotor.controlMotor(power, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(power, BasicMotorPort.FORWARD);
	}
	
	private static void driveBackward(int power) {
		leftMotor.controlMotor(power, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(power, BasicMotorPort.BACKWARD);
	}
	
	private static void turnRight(int power) {
		leftMotor.controlMotor(power, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(power, BasicMotorPort.BACKWARD);
	}
	
	private static void turnLeft(int power) {
		leftMotor.controlMotor(power, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(power, BasicMotorPort.FORWARD);
	}

	private static void move(MovementType movetype, int power) {
		switch (movetype) {
		case ForwardVerySlow:
		case ForwardSlow:
		case ForwardFast:
		case ForwardVeryFast:
			driveForward(power);
			break;
		case TurnLeft:
			turnLeft(power);
			break;
		case TurnRight:
			turnRight(power);
			break;
		case Backward:
			driveBackward(power);
			break;
		default:
			stahp();
			break;
		}
	}

	private static void toggleCollection() {
		switchModes();
		dataCollection = !dataCollection;
		setCollectionHeader();
		
		movementMode = MovementType.Stop;
		mode = Mode.Wait;
	}

	private static void switchModes() {
		if (dataCollection) {
			if (!results.isEmpty()) {
				writeArffFile(results);
			}

			results.clear();
		}
		
		if (agent != null) {
			agent.saveAgent(agentFile);
		}
	}

	private static void setCollectionHeader() {
		modeLbl.setText(dataCollection ? "DATA COLLECTION MODE\n\n"
				: "DOING STUFF MODE\n\n");
	}

	private static class ButtonHandler implements MouseListener, KeyListener {

		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent moe) {
		}

		public void mouseReleased(MouseEvent moe) {
			// If you click on the window it should remove focus from the text
			// fields (allowing us to use keyboard commands again)
			NXTrc.requestFocusInWindow();
		}

		// ***********************************************************************
		// Keyboard action
		public void keyPressed(KeyEvent ke) {
			char key = ke.getKeyChar();

			switch (key) {
			case '1':
				switchModes();

				if (dataCollection) {
					mode = Mode.Part1EM;
				} else if (resetWeka()) {
					mode = Mode.Part1EM;
				}
				break;
			case '2':
				switchModes();

				if (dataCollection) {
					mode = Mode.Part1KM;
				} else if (resetWeka()) {
					mode = Mode.Part1KM;
				}
				break;
				
			case '3':
				if (mode != Mode.Part3) {
					setupRL();
					mode = Mode.Part3;
				}
				break;
			case '4':
				if (mode != Mode.Part4 && resetWeka()) {
					setupRL();
					mode = Mode.Part4;
				}
				break;
				

			// Movement Modes!
			case 'u':
				movementMode = MovementType.ForwardVerySlow;
				break;
			case 'i':
				movementMode = MovementType.ForwardSlow;
				break;
			case 'o':
				movementMode = MovementType.ForwardFast;
				break;
			case 'p':
				movementMode = MovementType.ForwardVeryFast;
				break;
				
			case 'z':
				movementMode = MovementType.Stop;
				break;

			case 'c':
				toggleCollection();
				break;

			case 's':
				mode = Mode.Wait;
				break;

			case 'q':
				mode = Mode.Stop;
				break;
			}
		}

		public void keyTyped(KeyEvent ke) {
		}

		public void keyReleased(KeyEvent ke) {
		}
	}

	// Stahps.
	private static void stahp() {
		leftMotor.controlMotor(100, BasicMotorPort.STOP);
		rightMotor.controlMotor(100, BasicMotorPort.STOP);
	}
}
