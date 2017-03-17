public class TestSubsystem extends HBRSubsystem<TestSubsystem.Follower> {
	// The the systems being profiled/PID controlled
	public enum Follower {
		DISTANCE, ANGLE
	}
	
	// The singleton instance of this subsystem
	private static TestSubsystem instance;

	// Singleton getter
	public static TestSubsystem getInstance() {
		if(instance == null) {
			instance = new TestSubsystem();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem() {
		super("Test Subsystem 1", 1);
		System.out.println("TS");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem testSubsystem = TestSubsystem.getInstance();
		Follower follower = Follower.DISTANCE;
		testSubsystem.getFollowerIndex(follower);
		testSubsystem.resetIntegrator(follower);
		testSubsystem.setFeedback(follower, 1, 2, 3);
		testSubsystem.setFeedforward(follower, 4, 5, 6);
		testSubsystem.setILimit(follower, 7);
		testSubsystem.setSetpoint(follower, 15);
		testSubsystem.setOutputLimit(follower, 8);
		testSubsystem.setPIDMode(follower, PIDMode.VELOCITY);
		testSubsystem.setMode(follower, Mode.FOLLOWER);
		testSubsystem.setProfile(follower, new double[][] {{9, 10, 11},{12, 13, 14}});
		testSubsystem.pause(follower);
		testSubsystem.resume(follower);
		testSubsystem.rewind(follower);
	}

	@Override
	public void pidWrite(double[] output) {
		
	}

	@Override
	public double[] pidRead() {
		return new double[2];
	}
}