
public enum MovementType {
	Stop, ForwardVerySlow, ForwardSlow, ForwardFast, ForwardVeryFast, Backward, TurnRight, TurnLeft;
	
	// Weka classification gives us an integer
	public static MovementType intToMovementType(int intType) {
		switch(intType) {
		case 1:
			return ForwardVerySlow;
		case 2:
			return ForwardSlow;
		case 3:
			return ForwardFast;
		case 4:
			return ForwardVeryFast;
		case 5:
			return Backward;
		case 6:
			return TurnRight;
		case 7:
			return TurnLeft;
		default:
			return Stop;
		}
	}
	
	// Movement types for arff files
	public static String movementTypes() {
		return "{ Stop, ForwardVerySlow, ForwardSlow, ForwardFast, ForwardVeryFast, Backward, TurnRight, TurnLeft }";
	}
	
	public static int getPower(MovementType mode) {
		switch (mode) {
		case ForwardVerySlow:
			return 5;
		case ForwardSlow:
			return 10;
		case ForwardFast:
			return 15;
		case ForwardVeryFast:
			return 20;
		}
		
		return 0;
	}
}
