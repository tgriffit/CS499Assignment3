package ReinforcementLearning;

import JockeyControl.JockeyControl;
import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;
import environment.AbstractEnvironmentSingle;
import environment.ActionList;
import environment.IAction;
import environment.IState;

public class Environment extends AbstractEnvironmentSingle {
	
	private static final long serialVersionUID = -5473856690711387195L;
	
	// The sensors we have access to
	private UltrasonicSensor frontSensor;
	private UltrasonicSensor backSensor;
	private LightSensor lightSensor;
	
	// The distance from the wall to the middle of the track
	private int middleDist = 50;
	private int tolerance = 2;
	
	public Environment(UltrasonicSensor front, UltrasonicSensor back, LightSensor light) {
		frontSensor = front;
		backSensor = back;
		lightSensor = light;
	}

	@Override
	public ActionList getActionList(IState s) {
		ActionList list = new ActionList(s);
		list.add(new JockeyAction(Action.Forward));
		
		JockeyState js = (JockeyState)s;
		if (!(js.orientation == Orientation.Proper && js.distance == Distance.TooClose)) {
			list.add(new JockeyAction(Action.Left));
		}
		
		if (!(js.orientation == Orientation.Proper && js.distance == Distance.TooFar)) {
			list.add(new JockeyAction(Action.Right));
		}
		
		return list;
	}

	@Override
	public IState successorState(IState s, IAction a) {
		JockeyControl.takeAction(((JockeyAction)a).action);
		
		// Give the action a chance to change the state
		Delay.msDelay(10);
		
		return getCurrentState();
	}

	@Override
	public IState defaultInitialState() {
		return getCurrentState();
	}

	@Override
	public double getReward(IState s1, IState s2, IAction a) {
		JockeyState initial = (JockeyState)s1;
		JockeyState result = (JockeyState)s2;
		
		if (initial.orientation == Orientation.Proper && initial.distance == Distance.Proper
				&& result.orientation == Orientation.Proper && result.distance == Distance.Proper) {
			// Our greatest reward should be for maintaining the right course
			return 20;
		}
		else if (result.orientation == Orientation.Proper && result.distance == Distance.Proper) {
			return 10;
		}
		else if (result.orientation == Orientation.Proper) {
			return -5;
		}
		
		return -10;
	}

	@Override
	public boolean isFinal(IState s) {
		JockeyState js = (JockeyState)s;
		return js.onTape;
	}

	@Override
	public int whoWins(IState s) {
		JockeyState js = (JockeyState)s;
		if(js.orientation == Orientation.Proper && js.distance == Distance.Proper) {
			// Great success!
			return 1;
		}
		else {
			return -1;
		}
	}
	
	public IState getCurrentState() {
		System.out.println("Starting");
		JockeyState js = new JockeyState(this);
		System.out.println("Constructed");
//		js.orientation = distsToOrientation(frontSensor.getDistance(), backSensor.getDistance());
		js.orientation = distsToOrientation(0, 0);
		System.out.println("Orientation");
		js.distance = distsToDistance(frontSensor.getDistance(), backSensor.getDistance());
		System.out.println("Distance");
		js.onTape = lightSensor.getLightValue() < 35; // Super approximate, but only used to tell us when an episode is over
		
		System.out.println("Returning state");
		return js;
	}
	
	public Orientation distsToOrientation(int front, int back) {
		System.out.println("WHY");
		if (distsEqual(front, back)) {
			return Orientation.Proper;
		}
		else if (front > back) {
			return Orientation.AngledRight;
		}
		else {
			return Orientation.AngledLeft;
		}
	}
	
	public Distance distsToDistance(int front, int back) {
		int average = (front + back) / 2;
		
		if (distsEqual(average, middleDist)) {
			return Distance.Proper;
		}
		else if (average > middleDist) {
			return Distance.TooFar;
		}
		else {
			return Distance.TooClose;
		}
	}

	// Tests whether two distances are equal within the given tolerance
	private boolean distsEqual(int a, int b) {
		return Math.abs(a - b) <= tolerance;
	}
}
