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

public class DataCollection extends JFrame {
	public static DataCollection NXTrc;

	public static JLabel commands;
	public static ButtonHandler bh = new ButtonHandler();
	public static DataOutputStream outData;
	public static DataInputStream inData;
	public static NXTConnector link;

	public static JTextField t1, t2, t3, t4, t5, t6, t7, t8, t9;
	public static JButton arraySender;

	public DataCollection() {
		connect();

		setLayout(new FlowLayout());

		setTitle("Control Jockey");
		setBounds(650, 350, 400, 300);
		addMouseListener(bh);
		addKeyListener(bh);

		String cmds = "<html>Buttons:<br>"
				+ "z: Record Part 1 Data<br>"
				+ "c: Record Part 2 Data<br>"
				+ "b: Record Part 3 Data<br>"
				+ "<br>"
				+ "r: Save Results<br>"
				+ "<br>"
				+ "1: Stop<br>"
				+ "2: Go Forward<br>"
				+ "3: Go Forward (Fast)<br>"
				+ "4: Go Backward<br>"
				+ "5: Turn Right<br>"
				+ "6: Turn Left<br>"
				+ "<br>"
				+ "q: Quit</html>";

		commands = new JLabel(cmds);
		add(commands);
	}

	public static void main(String[] args) {
		NXTrc = new DataCollection();
		NXTrc.setVisible(true);
		NXTrc.requestFocusInWindow();
		NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

//	private static void writeArffFile(String filename, ArrayList<Result> results) {
//
//		try {
//			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
//
//			// Header!
//			out.write(results.get(0).header());
//
//			// Data!
//			out.write("@DATA\n");
//			for (int i = 0; i < results.size(); ++i) {
//				out.write(results.get(i).toString());
//			}
//
//			out.close();
//		} catch (IOException e) {
//
//		}
//
//		System.out.println("Saved as " + filename + "\n");
//	}

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
			try {
				char key = ke.getKeyChar();

				outData.writeChar(key);
				outData.flush();

				if (key == 'r') {
					//receiveData();
				}
			} catch (IOException ioe) {
				System.out.println("\nIO Exception writeInt");
			}
		}

		public void keyTyped(KeyEvent ke) {

		}

		public void keyReleased(KeyEvent ke) {
			 try {
			 switch (ke.getKeyChar()) {
			 case 'w':
			 outData.writeChar('W');
			 break;
			 case 's':
			 outData.writeChar('S');
			 break;
			 case 'a':
			 outData.writeChar('A');
			 break;
			 case 'd':
			 outData.writeChar('D');
			 break;
			 case 'q':
			 disconnect();
			 System.exit(0);
			 }
			
			 outData.flush();
			 }
			
			 catch (IOException ioe) {
			 System.out.println("\nIO Exception writeInt");
			 }
		}
	}

	public static void connect() {
		link = new NXTConnector();

		if (!link.connectTo("btspp://")) {
			System.out.println("\nNo NXT found.");
		}

		outData = new DataOutputStream(link.getOutputStream());
		inData = new DataInputStream(link.getInputStream());
		System.out.println("\nNXT is Connected");
	}

	public static void disconnect() {
		try {
			outData.close();
			inData.close();
			link.close();
		} catch (IOException ioe) {
			System.out.println("\nIO Exception writing bytes");
		}
		System.out.println("\nClosed data streams");
	}
}
