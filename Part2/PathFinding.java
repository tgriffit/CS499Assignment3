import java.util.ArrayList;

class Point {
	public double x;
	public double y;
	public int light;

	Point(double _x, double _y, int _light) {
		this.x = _x;
		this.y = _y;
		this.light = _light;
	}

	public String toString() {
		return "(x y l): (" + x + " " + y + " " + light + ")";
	}
}

public class PathFinding {
	ArrayList<Cluster> clusters;

	public PathFinding(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	public ArrayList<Point> findPath(Point start, Point end) {
		ArrayList<Point> path = new ArrayList<Point>();

		// find start and end point cluster numbers
		int startClustNum = -1;
		int endClustNum = -1;
		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			ArrayList<Point> ps = c.points;
			for (int j = 0; j < ps.size(); j++)
			{
				Point p = ps.get(j);
				if (p.x == start.x && p.y == start.y && p.light == 100) {
					startClustNum = i;
				} else if (p.x == end.x && p.y == end.y && p.light == 100) {
					endClustNum = i;
				}
			}
		}

		// determine which clusters are neighbours by terrible heuristic
		for (int i = 0; i < clusters.size(); i++) {
			Cluster home = clusters.get(i);
			for (int j = 0; j < clusters.size(); j++) {
				if (i == j)
					continue;
				else {
					Cluster target = clusters.get(j);
					double cdist = distBetween(home.average.x, home.average.y, target.average.x, target.average.y);
					Point closestPoint = target.average; 
					for (Point ph : home.points) {
						for (Point pt : target.points)
						{
							double tempDist = distBetween(ph.x, ph.y, pt.x, pt.y);
							if (tempDist < cdist) {
								cdist = tempDist;
								closestPoint = pt;
							}
						}
					}
					double dist = distBetween(home.average.x, home.average.y, 
											   closestPoint.x, closestPoint.y);
					if (dist <= 70.0) // MAGIC NUM FUNTIMES!
					{
						home.neighbors.add(j);
					}
				}
			}
		}
		
		clusters.get(3).neighbors.add(4);
		Cluster currClust = clusters.get(startClustNum);
		ArrayList<Integer> intpath = new ArrayList<Integer>();
		intpath.add(startClustNum);
		int currClustNum = startClustNum;
		path.add(currClust.average);
		while (currClustNum != endClustNum) 
		{
			for (Integer c: currClust.neighbors)
			{
				for (Integer r: intpath)
				{
					Cluster neighbor = clusters.get(c);
					if (neighbor.neighbors.contains(r))
						neighbor.neighbors.remove(r);
				}
			}
			
			int highestlight = Integer.MIN_VALUE;
			int highestNum = -1;
			for (int i = 0; i < currClust.neighbors.size(); i++) {
				int neighborNum = currClust.neighbors.get(i);
				if (clusters.get(neighborNum).average.light > highestlight) {
					highestNum = neighborNum;
					highestlight = clusters.get(neighborNum).average.light;
				}
			}
			
			currClustNum = highestNum;
			intpath.add(highestNum);
			currClust = clusters.get(highestNum);
			path.add(currClust.average);
		}
		
		return path;
	}

	private double distBetween(double x1, double y1, double x2, double y2) {
		double dx = (int)x2 - (int)x1;
		double dy = (int)y2 - (int)y1;

		return Math.sqrt(Math.abs(dx * dx - dy * dy));
	}
}