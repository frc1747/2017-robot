public class TestSubsystem7 extends HBRSubsystem<TestSubsystem7.Follower> {
	// The the systems being profiled/PID controlled
	public enum Follower {
		DISTANCE, ANGLE
	}
	
	// The singleton instance of this subsystem
	private static TestSubsystem7 instance;

	// Singleton getter
	public static TestSubsystem7 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem7();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem7() {
		super(.01);
	}

	// Testing methods
	public static void main(String[] args) {
		// Test of normal PID features
		TestSubsystem7 testSubsystem = TestSubsystem7.getInstance();

		testSubsystem.setMode(Follower.DISTANCE, Mode.FOLLOWER);
		testSubsystem.setPIDMode(Follower.DISTANCE, PIDMode.POSITION);
		testSubsystem.setILimit(Follower.DISTANCE, 10);
		testSubsystem.setFeedforward(Follower.DISTANCE, 0, 1, .1);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 2, 1, 0);
		
		testSubsystem.setMode(Follower.ANGLE, Mode.FOLLOWER);
		testSubsystem.setPIDMode(Follower.ANGLE, PIDMode.POSITION);
		testSubsystem.setILimit(Follower.ANGLE, 10);
		testSubsystem.setFeedforward(Follower.ANGLE, 0, 1, .1);
		testSubsystem.resetIntegrator(Follower.ANGLE);
		testSubsystem.setFeedback(Follower.ANGLE, 2, 1, 0);
		
		double[][][] profiles = HBRSubsystem.readProfilesFromFile(
				"C:\\Users\\Tiger\\Documents\\MotionProfiling\\profiles\\hopper3a_red.csv");
		testSubsystem.setProfile(Follower.DISTANCE, profiles[0]);
		testSubsystem.setProfile(Follower.ANGLE, profiles[1]);
		testSubsystem.resume(Follower.DISTANCE);
		testSubsystem.resume(Follower.ANGLE);

		while(testSubsystem.isRunning(Follower.DISTANCE) ||
				testSubsystem.isRunning(Follower.ANGLE))
			delay(1);
		System.exit(0);
	}
	
	double position = 0;
	double angle = 0;

	@Override
	public void pidWrite(double[] output) {
		System.out.println(position + "\t" + angle);
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
