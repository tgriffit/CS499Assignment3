import lejos.pc.comm.NXTConnector;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class Demo {
    public static TrackerReader tracker;

    public static void main (String[] args) {
        tracker = new TrackerReader();
        tracker.start();
        while (true) {
            Delay.msDelay(1000);
		System.out.println("reg: " +  tracker.x + " " + tracker.y + "theta " + tracker.theta);
        }

    }
}
