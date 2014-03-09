/*
 * This is based on code taken from http://www.lejos.org/forum/viewtopic.php?t=1723
 * originally written by Tawat Atigarbodee.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import lejos.pc.comm.NXTConnector;

class Result {
	String getHeader() {
		return "OH NOES THIS IS THE BASE CLASS. ABORT!";
	}
}

class Result1 extends Result {

}

public class DataCollection extends JFrame {
	public static DataCollection NXTrc;

	public static JLabel commands;
	public static ButtonHandler bh = new ButtonHandler();

	public static JTextField t1, t2, t3, t4, t5, t6, t7, t8, t9;
	public static JButton arraySender;

	public DataCollection() {
		setLayout(new FlowLayout());

		setTitle("Control Jockey");
		setBounds(650, 350, 400, 300);
		addMouseListener(bh);
		addKeyListener(bh);

		String cmds = "<html>Buttons:<br>" + "z: Record Part 1 Data<br>"
				+ "c: Record Part 2 Data<br>" + "b: Record Part 3 Data<br>"
				+ "<br>" + "r: Save Results<br>" + "<br>" + "1: Stop<br>"
				+ "2: Go Forward<br>" + "3: Go Forward (Fast)<br>"
				+ "4: Go Backward<br>" + "5: Turn Right<br>"
				+ "6: Turn Left<br>" + "<br>" + "q: Quit</html>";

		commands = new JLabel(cmds);
		add(commands);
	}

	public static void main(String[] args) {
		NXTrc = new DataCollection();
		NXTrc.setVisible(true);
		NXTrc.requestFocusInWindow();
		NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void writeArffFile(String filename, ArrayList<Result> results) {

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

			if (key == 'r') {
				// receiveData();
			}
		}

		public void keyTyped(KeyEvent ke) {

		}

		public void keyReleased(KeyEvent ke) {

		}
	}
}
