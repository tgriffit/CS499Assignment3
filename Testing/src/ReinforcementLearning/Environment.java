package ReinforcementLearning;

import java.util.Scanner;

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

	// The distance from the wall to the middle of the track
	private int middleDist = 44;
	private int tolerance = 2;

	@Override
	public ActionList getActionList(IState s) {
		ActionList list = new ActionList(s);
		
		list.add(new JockeyAction(Action.Forward));
		list.add(new JockeyAction(Action.GoLeft));
		list.add(new JockeyAction(Action.GoRight));
		
		return list;
	}

	@Override
	public IState successorState(IState s, IAction a) {
		JockeyAction ja = (JockeyAction) a;

		if (ja.action == Action.GoLeft) {
			JockeyControl.squiggleLeft();
		} else if (ja.action == Action.GoRight) {
			JockeyControl.squiggleRight();
		} else if (ja.action == Action.Forward) {
			JockeyControl.go();
		}

		// If our sensors aren't facing directly at the wall we have
		// no accurate way of knowing where we are, so the state transition
		// isn't complete until we straighten out
		JockeyControl.straighten();

		return getCurrentState();
	}

	@Override
	public IState defaultInitialState() {
		return getCurrentState();
	}

	@Override
	public double getReward(IState s1, IState s2, IAction a) {
		JockeyState initial = (JockeyState) s1;
		JockeyState result = (JockeyState) s2;
		JockeyAction ja = (JockeyAction) a;
		
		int reward = -10;

		// This is true when Jockey ends up on one of the side lines
		if (result.onTape && result.distance != Distance.Proper) {
			// No, bad Jockey!
			reward -= 5000;
		}
		
		if (initial.distance == Distance.Proper && ja.action == Action.Forward) {
			// Good job, Jockey!
				reward += 50;
		}
		else {
			// Encourages Jockey to move towards the middle faster
			reward -= getDistanceFromCenter();
		}
		
		if (initial.distance == Distance.TooClose && ja.action != Action.GoRight
				|| initial.distance == Distance.TooFar && ja.action != Action.GoLeft) {
			reward -= 10;
		}

		return reward;
	}

	@Override
	public boolean isFinal(IState s) {
		JockeyState js = (JockeyState) s;
		return js.onTape;
	}

	@Override
	public int whoWins(IState s) {
		JockeyState js = (JockeyState) s;
		if (js.orientation == Orientation.Proper
				&& js.distance == Distance.Proper) {
			// Great success!
			return 1;
		} else {
			return -1;
		}
	}

	public IState getCurrentState() {
		JockeyState js = new JockeyState(this);

		int front = JockeyControl.getFrontDistance();
		int back = JockeyControl.getBackDistance();
		int light = JockeyControl.getLightValue();

		js.orientation = distsToOrientation(front, back);
		js.distance = distsToDistance(front, back);
		js.onTape = light > 30; // Magic number - the reading on the felt is ~25, the reading on tape is ~35

		return js;
	}

	public Orientation distsToOrientation(int front, int back) {
		if (distsEqual(front, back)) {
			return Orientation.Proper;
		} else if (front > back) {
			return Orientation.AngledRight;
		} else {
			return Orientation.AngledLeft;
		}
	}

	public Distance distsToDistance(int front, int back) {
		int average = (front + back) / 2;

		if (distsEqual(average, middleDist)) {
			return Distance.Proper;
		} else if (average > middleDist) {
			return Distance.TooFar;
		} else {
			return Distance.TooClose;
		}
	}

	// Tests whether two distances are equal within the given tolerance
	private boolean distsEqual(int a, int b) {
		return Math.abs(a - b) <= tolerance;
	}
	
	private double getDistanceFromCenter() {
		int front = JockeyControl.getFrontDistance();
		int back = JockeyControl.getBackDistance();
		double average = (front + back) / 2.0;
		
		return Math.abs(average - middleDist);
	}
}
