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