public class TestSubsystem3 extends HBRSubsystem<TestSubsystem3.Profile> {
	// The the systems being profiled/PID controlled
	public enum Profile {}

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
		super();
		System.out.println("TS3");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem3 testSubsystem = TestSubsystem3.getInstance();
	}
}