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
	public ArrayList<Cluster> clusters;
	int diameter = 6;

	public PathFrame(ArrayList<Point> _path, ArrayList<Cluster> _clusters) {
		this.path = _path;
		this.clusters = _clusters;
		setSize(new Dimension(600, 440));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void paint(Graphics g) {
		// draw the clusters in the background
		drawClusters(g, clusters);
		
		// now draw the path in red over top
		g.setColor(Color.red);
		for (Point p : path)
			g.fillOval((int)p.x-diameter/2, (int)p.y-diameter/2, diameter, diameter);
		
		for (int i = 0; i < path.size()-1; i++)
		{
			Point p1 = path.get(i);
			Point p2 = path.get(i+1);
			g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		}
	}
	
	public void drawClusters(Graphics g, ArrayList<Cluster> clusters) {
		Color[] colours = {Color.black, Color.blue, Color.green, Color.cyan, Color.DARK_GRAY, Color.lightGray, Color.MAGENTA, Color.ORANGE, Color.YELLOW};
		int colourIndex = 0;
		// draw cluster points
		for (Cluster c : clusters) {
			g.setColor(colours[colourIndex]);
			colourIndex++;
			colourIndex = colourIndex % colours.length;
			for (Point p: c.points)
				g.fillOval((int)p.x-diameter/2, (int)p.y-diameter/2, diameter, diameter);
		}
	}

}