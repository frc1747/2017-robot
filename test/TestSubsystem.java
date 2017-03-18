import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		SmartDashboard.putData("TestSubsystem", this);
	}

	// Testing methods
	public static void main(String[] args) {
		TestSubsystem testSubsystem = TestSubsystem.getInstance();
		System.out.println(testSubsystem.getFollowerIndex(Follower.ANGLE));
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