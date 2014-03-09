
public enum MovementType {
	Stop, Forward, ForwardFast, Backward, TurnRight, TurnLeft;
	
	public static MovementType intToMovementType(int intType) {
		switch(intType) {
		case 0:
			return Forward;
		case 1:
			return ForwardFast;
		case 2:
			return Backward;
		case 3:
			return TurnRight;
		case 4:
			return TurnLeft;
		default:
			return Stop;
		}
	}
}
