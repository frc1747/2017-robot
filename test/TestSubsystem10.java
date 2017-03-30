import java.util.logging.Level;

import lib.frc1747.instrumentation.Instrumentation;
import lib.frc1747.instrumentation.Logger;

public class TestSubsystem10 extends HBRSubsystem {

	// The singleton instance of this subsystem
	private static TestSubsystem10 instance;

	// Singleton getter
	public static TestSubsystem10 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem10();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem10() {
	}
	
	enum test {
		NONE, HOPPER, DRIVE_STRAIGHT
	}

	// Testing methods
	public static void main(String[] args) {
		new AutonChooser(test.class);
	}
	
	double position = 0;
	double angle = 0;

	@Override
	public void pidWrite(double[] output) {
		//System.out.println(position + "\t" + angle);
		//Instrumentation.getLogger("TestSubsystem9").log(Level.FINE, "%.4f\t%.4f", position, angle);
		Instrumentation.getLogger("TestSubsystem9").putDouble("position", position);
		Instrumentation.getLogger("TestSubsystem9").putDouble("angle", angle);
		position += output[0] * 1.5 * 0.01;
		angle += output[1] * 1.5 * 0.01;
	}

	@Override
	public double[] pidRead() {
		return new double[] {position, angle};
	}
	
	public static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
