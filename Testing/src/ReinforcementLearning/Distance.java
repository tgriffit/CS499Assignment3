package ReinforcementLearning;

public enum Distance { 
	TooClose, Proper, TooFar;
	
	public static int toInt(Distance d) {
		switch (d) {
		case TooClose:
			return 0;
		case Proper:
			return 1;
		case TooFar:
			return 2;
		default:
			return 0;	
		}
	}
}
