import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import lejos.nxt.*;
import lejos.pc.comm.NXTConnector;
import lejos.util.Delay;

public class Lerning extends JFrame {

	private static WekaHandler weka;

	private enum Mode {
		Stop, Wait, Part1, Part2, Part3
	}

	private static Mode mode = Mode.Wait;

	static MotorPort leftMotor = MotorPort.C;
	static MotorPort rightMotor = MotorPort.A;

	static LightSensor lightSensor = new LightSensor(SensorPort.S4);
	static UltrasonicSensor ultrasoundSensor = new UltrasonicSensor(SensorPort.S3);
	static OpticalDistanceSensor rightIR = new OpticalDistanceSensor(SensorPort.S2);
	static OpticalDistanceSensor leftIR = new OpticalDistanceSensor(SensorPort.S1);

	static boolean sensorsOn = false;
	static boolean lineFollowMode = false;

	public static Lerning NXTrc;

	public static JLabel commands;
	public static ButtonHandler bh = new ButtonHandler();

	public Lerning() {

		setLayout(new FlowLayout());

		setTitle("Jockey Self-Control");
		setBounds(400, 350, 300, 200);
		addMouseListener(bh);
		addKeyListener(bh);

		String cmds = "<html>Buttons:<br>" 
				+ "1: Part 1 (Line Follow)<br>"		
				+ "2: Part 2 (Move Within Track)<br>"
				+ "3: Part 3 (Obstacle Course Line Follow)<br>"
				+ "<br>"
				+ "s: Stop<br>" 
				+ "<br>" 
				+ "q: Quit</html>";

		commands = new JLabel(cmds);
		add(commands);
	}

	public static void main(String[] args) {
		NXTrc = new Lerning();
		NXTrc.setVisible(true);
		NXTrc.requestFocusInWindow();
		NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		disableSensors();

		while (mode != Mode.Stop) {
			switch (mode) {
			case Part1:
				doWekaPart1();
				break;
			case Part2:
				doWekaPart2();
				break;
			case Part3:
				doWekaPart3();
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

	private static void doWekaPart1() {
		if (sensorsOn) {
			boolean result = weka.getPart1Classification(lightSensor
					.getLightValue());

			if (result) {
				turnLeft();
			} else {
				turnRight();
			}
		}
	}

	private static void doWekaPart2() {
		if (sensorsOn) {
			MovementType result = weka
					.getPart2Classification(lightSensor.getLightValue(),
							ultrasoundSensor.getDistance());
			move(result);
		}
	}

	private static void doWekaPart3() {
		if (sensorsOn) {
			MovementType result;

			if (lineFollowMode) {
				result = weka.getPart3ClassificationNoUltrasound(
						lightSensor.getLightValue(), rightIR.getDistance(),
						leftIR.getDistance());
			} else {
				result = weka.getPart3Classification(
						lightSensor.getLightValue(),
						ultrasoundSensor.getDistance(), rightIR.getDistance(),
						leftIR.getDistance());
			}

			move(result);
		}
	}

	private static boolean resetWeka() {
		final JFileChooser fc = new JFileChooser();
		int ret = fc.showOpenDialog(NXTrc);
		String modelfile, arffile;
		arffile = modelfile = "";

		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				modelfile = fc.getSelectedFile().getPath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}

		ret = fc.showOpenDialog(NXTrc);
		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				arffile = fc.getSelectedFile().getPath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}

		try {
			weka = new WekaHandler(modelfile, arffile, 1);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// Movement functions for Part 1
	private static void turnLeft() {
		leftMotor.controlMotor(0, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(30, BasicMotorPort.FORWARD);
	}

	private static void turnRight() {
		leftMotor.controlMotor(20, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(5, BasicMotorPort.BACKWARD);
	}
	
	// Movement functions for Part 2 and 3
	private static void driveForward() {
		leftMotor.controlMotor(15, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(15, BasicMotorPort.FORWARD);
	}

	private static void driveForwardFast() {
		leftMotor.controlMotor(50, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(50, BasicMotorPort.FORWARD);
	}

	private static void driveBack() {
		leftMotor.controlMotor(30, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(30, BasicMotorPort.BACKWARD);
	}

	private static void turnRightInPlace() {
		leftMotor.controlMotor(20, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(20, BasicMotorPort.BACKWARD);
	}

	private static void turnLeftInPlace() {
		leftMotor.controlMotor(20, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(20, BasicMotorPort.FORWARD);
	}

	// Movement functions for part 3
	private static void backAndTurnLeft() {
		leftMotor.controlMotor(20, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(12, BasicMotorPort.FORWARD);
	}

	private static void arcForwardAndRight() {
		leftMotor.controlMotor(20, BasicMotorPort.FORWARD);
		rightMotor.controlMotor(15, BasicMotorPort.FORWARD);
	}

	private static void driveBackSlow() {
		leftMotor.controlMotor(30, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(30, BasicMotorPort.BACKWARD);
	}

	private static void hardLeft() {
		leftMotor.controlMotor(40, BasicMotorPort.BACKWARD);
		rightMotor.controlMotor(30, BasicMotorPort.FORWARD);
	}

	private static void move(MovementType movetype) {
		switch (movetype) {
		case Backward:
			if (lineFollowMode) {
				driveBackSlow();
			} else {
				driveBack();
			}
			break;
		case Forward:
			if (lineFollowMode) {
				arcForwardAndRight();
			} else {
				driveForward();
			}
			break;
		case ForwardFast:
			if (lineFollowMode) {
				arcForwardAndRight();
			} else {
				driveForward();
			}
			break;
		case Stop:
			stahp();
			break;
		case TurnLeft:
			if (lineFollowMode) {
				backAndTurnLeft();
			} else {
				turnLeftInPlace();
			}
			break;
		case TurnRight:
			turnRightInPlace();
			break;
		default:
			break;
		}
	}

	// Saves power
	private static void disableSensors() {
		sensorsOn = false;
		ultrasoundSensor.setMode(UltrasonicSensor.MODE_OFF);
		rightIR.powerOff();
		leftIR.powerOff();
	}

	private static void enableSensors() {
		sensorsOn = true;
		ultrasoundSensor.setMode(UltrasonicSensor.MODE_CONTINUOUS);
		rightIR.powerOn();
		leftIR.powerOn();
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
				if (resetWeka()) {
					enableSensors();
					lineFollowMode = false;
					mode = Mode.Part1;
				}
				break;
			case '2':
				if (resetWeka()) {
					enableSensors();
					lineFollowMode = false;
					mode = Mode.Part2;
				}
				break;
			case '3':
				if (resetWeka()) {
					enableSensors();
					lineFollowMode = true;
					mode = Mode.Part3;
				}
				break;
			case '4':
				if (resetWeka()) {
					enableSensors();
					lineFollowMode = false;
					mode = Mode.Part3;
				}
				break;

			case 's':
				mode = Mode.Wait;
				disableSensors();
				break;
				
			case 'm':
				playMusic();
				break;

			case 'q':
				mode = Mode.Stop;
				disableSensors();
				break;
			}
		}

		public void keyTyped(KeyEvent ke) {
		}

		public void keyReleased(KeyEvent ke) {
		}
	}
	
	private static void playMusic() {
		Sound.buzz();
	}

	// Stahps.
	private static void stahp() {
		leftMotor.controlMotor(100, BasicMotorPort.STOP);
		rightMotor.controlMotor(100, BasicMotorPort.STOP);
	}
}
