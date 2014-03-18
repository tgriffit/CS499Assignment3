import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class UIHandler {

	public static enum Mode {
		Record, Pause, Test, Demo
	}

	public String arfffile = null;
	public Mode mode;
	
	Part2PC parent;
	JFrame guiFrame;
	PathFrame imageFrame;
	JTextArea cmdText;
	JTextArea modeDisplay;

	public UIHandler(Part2PC p2) {
		this.parent = p2;
		guiFrame = new JFrame();
		this.mode = Mode.Pause;

		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Creating a Table Example");
		guiFrame.setSize(400, 118);
		guiFrame.setLocationRelativeTo(null);

		// add a text area to show what the current mode is
		modeDisplay = new JTextArea();
		updateModeDisplay();

		// set up the text area
		int rows = 4;
		int cols = 50;
		cmdText = new JTextArea(rows, cols);
		cmdText.setEditable(false);
		cmdText.append("Press:\t't' to enter test mode\n");
		cmdText.append("\t'r' to enter record mode\n");
		cmdText.append("\t'p' to pause\n");
		cmdText.append("\t's' to save recorded data from record mode\n");
		cmdText.append("\t'd' to demo part 2");

		// add a keylistener to the text box
		cmdText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 't':
					mode = Mode.Test;
					break;
				case 'r':
					mode = Mode.Record;
					break;
				case 'p':
					mode = Mode.Pause;
					break;
				case 's':
					parent.saveData();
					break;
				case 'd':
					mode = Mode.Demo;
					break;
				default:
				}
				updateModeDisplay();
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		guiFrame.add(cmdText, BorderLayout.NORTH);
		guiFrame.add(modeDisplay, BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}
	
	public String getArffname() {
		if (arfffile == null)
			arfffile = "centerdata.arff";
		return this.arfffile;
	}
	
	private void updateModeDisplay() {
		modeDisplay.setText("Current Mode: [" + mode.toString() + "]");
	}

	public void drawPath(ArrayList<Point> path, Point topleft, Point topright) {
		imageFrame = new PathFrame(path);
	}
	
	public Mode getMode() {
		return mode;
	}
}