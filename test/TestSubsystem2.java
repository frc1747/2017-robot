public class TestSubsystem2 extends TestSubsystem {
	// The singleton instance of this subsystem
	private static TestSubsystem2 instance;

	// Singleton getter
	public static TestSubsystem2 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem2();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem2() {
		super();
		System.out.println("TS2");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem2 testSubsystem = TestSubsystem2.getInstance();
	}
}