package ReinforcementLearning;

import environment.AbstractState;
import environment.IEnvironment;
import environment.IState;

public class JockeyState extends AbstractState {
	
	private static final long serialVersionUID = -4747095610433386640L;
	
	public Orientation orientation;
	public Distance distance;
	
	public boolean onTape;

	public JockeyState(IEnvironment ct) {
		super(ct);
	}

	@Override
	public IState copy() {
		// TODO Auto-generated method stub
		JockeyState js = new JockeyState(myEnvironment);
		js.orientation = orientation;
		js.distance = distance;
		js.onTape = onTape;
		
		return js;
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
