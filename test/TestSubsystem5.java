public class TestSubsystem5 extends HBRSubsystem<TestSubsystem5.Follower> {
	// The the systems being profiled/PID controlled
	public enum Follower {
		DISTANCE, ANGLE
	}
	
	// The singleton instance of this subsystem
	private static TestSubsystem5 instance;

	// Singleton getter
	public static TestSubsystem5 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem5();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem5() {
		super(.1);
	}

	// Testing methods
	public static void main(String[] args) {
		// Test of normal PID features
		TestSubsystem5 testSubsystem = TestSubsystem5.getInstance();
		
		testSubsystem.setMode(Follower.DISTANCE, Mode.PID);
		testSubsystem.setPIDMode(Follower.DISTANCE, PIDMode.POSITION);
		testSubsystem.setILimit(Follower.DISTANCE, 10);
		testSubsystem.setFeedforward(Follower.DISTANCE, 0, 0, 0);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 1, 0, 0);
		testSubsystem.setSetpoint(Follower.DISTANCE, -2);

		delay(5000);
		testSubsystem.setSetpoint(Follower.DISTANCE, 2);
		
		delay(5000);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 1, 1, 0);
		testSubsystem.setSetpoint(Follower.DISTANCE, -2);
		
		delay(5000);
		testSubsystem.setSetpoint(Follower.DISTANCE, 2);
		
		delay(5000);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 2, 2, 0);
		testSubsystem.setSetpoint(Follower.DISTANCE, -2);
		
		delay(5000);
		testSubsystem.setSetpoint(Follower.DISTANCE, 2);
		
		delay(5000);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 2, 2, .3);
		testSubsystem.setSetpoint(Follower.DISTANCE, -2);
		
		delay(5000);
		testSubsystem.setSetpoint(Follower.DISTANCE, 2);
		
		delay(5000);
		System.exit(0);
	}
	
	double position = 0;

	@Override
	public void pidWrite(double[] output) {
		System.out.println(output[0] + "\t" + position);
		position += output[0] * 1.5 * 0.1;
	}

	@Override
	public double[] pidRead() {
		return new double[] {position, 0};
	}
	
	public static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}