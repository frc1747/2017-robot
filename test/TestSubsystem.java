public class TestSubsystem extends HBRSubsystem<TestSubsystem.Profile> {
	// The the systems being profiled/PID controlled
	public enum Profile {
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
		super();
		System.out.println("TS");
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem testSubsystem = TestSubsystem.getInstance();
		System.out.println(testSubsystem.getProfileIndex(Profile.ANGLE));
	}
}