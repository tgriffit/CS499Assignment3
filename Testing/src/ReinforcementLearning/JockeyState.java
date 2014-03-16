package ReinforcementLearning;

import environment.AbstractState;
import environment.IEnvironment;
import environment.IState;

public class JockeyState extends AbstractState {
	
	public Orientation orientation;
	public Distance distance;

	public JockeyState(IEnvironment ct) {
		super(ct);
	}

	@Override
	public IState copy() {
		// TODO Auto-generated method stub
		return null;
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
