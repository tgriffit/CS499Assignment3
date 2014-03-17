package ReinforcementLearning;

public enum Action { 
	Forward, GoLeft, GoRight, Stop;
	
	public static int toInt(Action a) {
		switch (a) {
		case Forward:
			return 1;
		case GoLeft:
			return 2;
		case GoRight:
			return 3;
		default:
			return 0;
		}
	}
}
