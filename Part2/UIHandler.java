import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

//Here's a class for a simple GUI that uses a JFrame
//to hold to JTextAreas - one will listen for the key events
//and the other will sit inside a JScrollPane providing feedback
//about the KeyListener events being triggered

public class UIHandler {

	public static enum Mode {
		Record, Pause, Test
	}

	public String arfffile = null;
	public Mode mode;
	
	Part2PC parent;
	JFrame guiFrame;
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
				default:
				}
				updateModeDisplay();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		guiFrame.add(cmdText, BorderLayout.NORTH);
		guiFrame.add(modeDisplay, BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}

	private void getArffFromUser() {
		final JFileChooser fc = new JFileChooser();

		if (fc.showOpenDialog(guiFrame) == JFileChooser.APPROVE_OPTION) {
			try {
				this.arfffile = fc.getSelectedFile().getPath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error getting arff filename");
		}
	}
	
	public String getArffname() {
		if (arfffile == null)
			getArffFromUser();
		return this.arfffile;
	}
	
	private void updateModeDisplay() {
		modeDisplay.setText("Current Mode: [" + mode.toString() + "]");
	}

	public Mode getMode() {
		return mode;
	}
}