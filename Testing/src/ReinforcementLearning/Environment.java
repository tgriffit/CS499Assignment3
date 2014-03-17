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

		//return getBadActionList(s);
		
		return list;
	}

	// Only gives a list of the correct action for each state
	public ActionList getBadActionList(IState s) {
		ActionList list = new ActionList(s);
		
		JockeyState js = (JockeyState) s;
		if (!(js.distance == Distance.TooClose)) {
			list.add(new JockeyAction(Action.GoLeft));
		} else if (!(js.distance == Distance.TooFar)) {
			list.add(new JockeyAction(Action.GoRight));
		} else {
			list.add(new JockeyAction(Action.Forward));
//			list.add(new JockeyAction(Action.GoLeft));
//			list.add(new JockeyAction(Action.GoRight));
		}
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
		int reward = 0;

		if (result.onTape) {
			System.out.print("Custom reward value: ");
			Scanner sc = new Scanner(System.in);
			int input = sc.nextInt();
			reward += input;
		}

		if (initial.orientation == Orientation.Proper
				&& initial.distance == Distance.Proper
				&& result.orientation == Orientation.Proper
				&& result.distance == Distance.Proper) {
			// Our greatest reward should be for maintaining the right course
			return ja.action == Action.Forward ? 50 : 20;
		} else if (result.orientation == Orientation.Proper
				&& result.distance == Distance.Proper) {
			return 10;
		} else if (result.orientation == Orientation.Proper) {
			reward -= 5;
		}

		if (initial.distance == Distance.TooClose && ja.action != Action.GoRight
				|| initial.distance == Distance.TooFar && ja.action != Action.GoLeft) {
			reward -= 10;
		}

		reward -= 10;

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
		js.onTape = light > 30; // Super approximate, but only used to tell us
								// when an episode is over

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
}
