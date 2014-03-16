package ReinforcementLearning;

import environment.IAction;

public class JockeyAction implements IAction {

	public Action action;
	
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

}
