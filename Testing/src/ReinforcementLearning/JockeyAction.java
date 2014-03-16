package ReinforcementLearning;

import environment.IAction;

public class JockeyAction implements IAction {

	public Action action;
	
	// The resultant distances of the action, needed to calculate the new state
	public int frontResult;
	public int backResult;
	public int gyroscope;
	
	public JockeyAction() {
		action = Action.Forward;
	}
	
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
	
	public void addResults(int front, int back, int gyro) {
		frontResult = front;
		backResult = back;
		gyroscope = gyro;
	}
}
