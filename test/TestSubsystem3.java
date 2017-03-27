public class TestSubsystem3 extends HBRSubsystem<TestSubsystem3.Follower> {
	// The the systems being profiled/PID controlled
	public enum Follower {}

	// The singleton instance of this subsystem
	private static TestSubsystem3 instance;
	
	// Singleton getter
	public static TestSubsystem3 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem3();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem3() {
		super("Test Subsystem 3", 0.01);
		System.out.println("TS3");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem3 testSubsystem = TestSubsystem3.getInstance();
		Follower follower = null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] pidRead() {
		// TODO Auto-generated method stub
		return null;
	}
}