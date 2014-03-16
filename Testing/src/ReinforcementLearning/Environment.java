package ReinforcementLearning;

import environment.AbstractEnvironmentSingle;
import environment.ActionList;
import environment.IAction;
import environment.IState;

public class Environment extends AbstractEnvironmentSingle {
	
	// The sensors we have access to
	
	// The distance from the wall to the middle of the track
	private int middleDist = 50;
	private int tolerance = 2;

	@Override
	public ActionList getActionList(IState s) {
		ActionList list = new ActionList(s);
		list.add(new JockeyAction(Action.Forward));
		list.add(new JockeyAction(Action.Left));
		list.add(new JockeyAction(Action.Right));
		
		return list;
	}

	@Override
	public IState successorState(IState s, IAction a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IState defaultInitialState() {
		// TODO Auto-generated method stub
		return null;
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
		else if (result.orientation == )
		
		return 0;
	}

	@Override
	public boolean isFinal(IState s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int whoWins(IState s) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Orientation distsToOrientation(int front, int back) {
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
		if (distsEqual(front, middleDist) && distsEqual(back, middleDist)) {
			return Distance.Proper;
		}
		else if (distsEqual(front, middleDist) != distsEqual(back, middleDist)) {
			// We're straddling the line. I guess that's good?
			return Distance.Proper;
		}
		else if (front > middleDist) {
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
