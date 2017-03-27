public class TestSubsystem4 extends HBRSubsystem {

	// The singleton instance of this subsystem
	private static TestSubsystem4 instance;
	
	// Singleton getter
	public static TestSubsystem4 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem4();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem4() {
		super("Test Subsystem 4", 0.01);
		System.out.println("TS4");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem4 testSubsystem = TestSubsystem4.getInstance();
		testSubsystem.getFollowerIndex(null);
		testSubsystem.resetIntegrator(null);
		testSubsystem.setFeedback(null, 1, 2, 3);
		testSubsystem.setFeedforward(null, 4, 5, 6);
		testSubsystem.setILimit(null, 7);
		testSubsystem.setSetpoint(null, 15);
		testSubsystem.setOutputLimit(null, 8);
		testSubsystem.setPIDMode(null, PIDMode.VELOCITY);
		testSubsystem.setMode(null, Mode.FOLLOWER);
		testSubsystem.setProfile(null, new double[][] {{9, 10, 11},{12, 13, 14}});
		testSubsystem.pause(null);
		testSubsystem.resume(null);
		testSubsystem.rewind(null);
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