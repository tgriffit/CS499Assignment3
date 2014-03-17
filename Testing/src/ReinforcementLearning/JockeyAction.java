package ReinforcementLearning;

import environment.IAction;

public class JockeyAction implements IAction {

	private static final long serialVersionUID = 8208419110166123403L;

	public Action action;
	
	// The resultant distances of the action, needed to calculate the new state
	public int frontResult;
	public int backResult;
	
	public JockeyAction(Action act) {
		action = act;
	}
	
	@Override
	public Object copy() {
		return new JockeyAction(action);
	}

	@Override
	public int nnCodingSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] nnCoding() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addResults(int front, int back) {
		frontResult = front;
		backResult = back;
	}
	
	@Override
	public int hashCode() {
		return Action.toInt(action);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof JockeyAction) {
			JockeyAction ja = (JockeyAction)o;
			return ja.action == action;
		}
		
		return false;
	}
}
