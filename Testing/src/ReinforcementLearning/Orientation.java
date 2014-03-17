package ReinforcementLearning;

public enum Orientation {
	
	AngledLeft, Proper, AngledRight;
	
	public static int toInt(Orientation o) {
		switch (o) {
		case AngledLeft:
			return 0;
		case Proper:
			return 10;
		case AngledRight:
			return 20;
		default:
			return 0;	
		}
	}
}
