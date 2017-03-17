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