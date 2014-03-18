import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PathFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public ArrayList<Point> path;

	public PathFrame(ArrayList<Point> _path) {
		this.path = _path;
		setSize(new Dimension(600, 440));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void paint(Graphics g) {
		int diameter = 6;
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("clusterdata1.png"));
		} catch (IOException e) {
			System.out.println("Error loading clusterdata1.png");
		}
		g.setColor(Color.red);
		g.drawImage(img, 20, 20, null);
		for (Point p : path)
			g.fillOval((int)p.x-diameter/2, (int)p.y-diameter/2, diameter, diameter);
		
		for (int i = 0; i < path.size()-1; i++)
		{
			Point p1 = path.get(i);
			Point p2 = path.get(i+1);
			g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		}
	}

}