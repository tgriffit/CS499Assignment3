import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.clusterers.SelfOrganizingMap;

class Cluster {
	public ArrayList<Point> points;
	public Point average;	// average x, y, and light for whole cluster
	public ArrayList<Integer> neighbors;
	
	public Cluster(ArrayList<Point> ps) {
		this.points = ps;
		this.neighbors = new ArrayList<Integer>();
		calcAvg();
	}
	
	public Cluster() {
		this.points = new ArrayList<Point>();
		this.neighbors = new ArrayList<Integer>();
	}
	
	private void calcAvg() {
		double x = 0;
		double y = 0;
		double lval = 0;
		for (Point p : points) {
			x += p.x;
			y += p.y;
			lval += p.light;
		}
		x /= points.size();
		y /= points.size();
		lval /= points.size();
		
		this.average = new Point(x, y, (int)lval);
	}
	
	// really slow to do it this way... but it should work 
	public void addPoint(Point p) {
		points.add(p);
		calcAvg();
	}
}

public class WekaHandler {
	private SelfOrganizingMap som;
	private Instances dataSet;

	// This was loosely based on the following code (but has changed
	// substantially):
	// http://stackoverflow.com/questions/8212980/weka-example-simple-classification-of-lines-of-text
	public WekaHandler(String arfffile) {
		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(new File(arfffile));
			dataSet = loader.getStructure();
		} catch (IOException e) {
			System.out.println("Error loading arff file)");
		}

		Instance i;
		try {
			while ((i = loader.getNextInstance(dataSet)) != null) {
				dataSet.add(i);
			}
		} catch (IOException e) {
			System.out.println("Error adding instances to dataSet");
		}

		try {
			som = (SelfOrganizingMap) SerializationHelper
					.read(new FileInputStream("SoMcenter.model"));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find model file to load");
		} catch (Exception e) {
			System.out
					.println("Something else when wrong loading the model file");
		}
	}

	public int getClusterNum(double x, double y, double light) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("x"), x);
		instance.setValue(dataSet.attribute("y"), y);
		instance.setValue(dataSet.attribute("light"), (int)light);

		// PREDICTION
		int cluster = -1;
		try {
			cluster = som.clusterInstance(instance);
		} catch (Exception e) {
			System.out.println("Error getting prediction in wekahandler");
		}
		return cluster;
	}

	public ArrayList<Cluster> getClusters(int topx, int topy, int botx, int boty) {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		ArrayList<ArrayList<Point>> matrix = null;
		
		matrix = getPointMatrix(topx, topy, botx, boty);
		
		for (int i = 0; i < matrix.size(); i++)
			clusters.add(new Cluster(matrix.get(i)));
		
		return clusters;
	}
	
	private ArrayList<ArrayList<Point>> getPointMatrix(int topx, int topy, int botx,
			int boty) {
		// top(x,y) is upper left, bot(x,y) is bottom right
		int height = boty - topy;
		int width = botx - topx;
		ArrayList<ArrayList<Point>> ret = new ArrayList<ArrayList<Point>>();
		
		// initialize for number of clusters
		try {
			for (int i = 0; i < som.numberOfClusters(); i++) 
					ret.add(new ArrayList<Point>());
		} catch (Exception e) {
			System.out.println("Exception on numberOfClusters, SoM maybe not init");
		}
		
		ListIterator<Instance> iter = dataSet.listIterator();
		double x, y, l;
		int clusterNum;
		while (iter.hasNext()) {
			Instance i = iter.next();
			x = i.value(0);
			y = i.value(1);
			l = i.value(2);
			clusterNum = getClusterNum(x, y, l);
			ret.get(clusterNum).add(new Point(x, y, (int) l));
		}
		
		return ret;
	}
}
