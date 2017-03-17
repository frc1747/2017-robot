public class TestSubsystem6 extends HBRSubsystem<TestSubsystem6.Follower> {
	// The the systems being profiled/PID controlled
	public enum Follower {
		DISTANCE, ANGLE
	}
	
	// The singleton instance of this subsystem
	private static TestSubsystem6 instance;

	// Singleton getter
	public static TestSubsystem6 getInstance() {
		if(instance == null) {
			instance = new TestSubsystem6();
		}
		return instance;
	}

	// Prevent direct initialization Constructor
	protected TestSubsystem6() {
		super(.1);
	}

	// Testing methods
	public static void main(String[] args) {
		// Test of normal PID features
		TestSubsystem6 testSubsystem = TestSubsystem6.getInstance();
		
		testSubsystem.setMode(Follower.DISTANCE, Mode.FOLLOWER);
		testSubsystem.setPIDMode(Follower.DISTANCE, PIDMode.POSITION);
		testSubsystem.setILimit(Follower.DISTANCE, 10);
		testSubsystem.setFeedforward(Follower.DISTANCE, 0, 1, .2);
		testSubsystem.resetIntegrator(Follower.DISTANCE);
		testSubsystem.setFeedback(Follower.DISTANCE, 2, 1, 0);
		double[][] profile = {
		{0	,0	,0},
		{0.005	,0.05	,0.5},
		{0.015	,0.1	,0.5},
		{0.03	,0.15	,0.5},
		{0.05	,0.2	,0.5},
		{0.075	,0.25	,0.5},
		{0.105	,0.3	,0.5},
		{0.14	,0.35	,0.5},
		{0.18	,0.4	,0.5},
		{0.225	,0.45	,0.5},
		{0.275	,0.5	,0.5},
		{0.33	,0.55	,0.5},
		{0.39	,0.6	,0.5},
		{0.455	,0.65	,0.5},
		{0.525	,0.7	,0.5},
		{0.6	,0.75	,0.5},
		{0.68	,0.8	,0.5},
		{0.765	,0.85	,0.5},
		{0.855	,0.9	,0.5},
		{0.95	,0.95	,0.5},
		{1.05	,1	,0.5},
		{1.15	,1	,0},
		{1.25	,1	,0},
		{1.35	,1	,0},
		{1.45	,1	,0},
		{1.55	,1	,0},
		{1.65	,1	,0},
		{1.75	,1	,0},
		{1.85	,1	,0},
		{1.95	,1	,0},
		{2.05	,1	,0},
		{2.15	,1	,0},
		{2.25	,1	,0},
		{2.35	,1	,0},
		{2.45	,1	,0},
		{2.55	,1	,0},
		{2.65	,1	,0},
		{2.75	,1	,0},
		{2.85	,1	,0},
		{2.95	,1	,0},
		{3.045	,0.95	,-0.5},
		{3.135	,0.9	,-0.5},
		{3.22	,0.85	,-0.5},
		{3.3	,0.8	,-0.5},
		{3.375	,0.75	,-0.5},
		{3.445	,0.7	,-0.5},
		{3.51	,0.65	,-0.5},
		{3.57	,0.6	,-0.5},
		{3.625	,0.55	,-0.5},
		{3.675	,0.5	,-0.5},
		{3.72	,0.45	,-0.5},
		{3.76	,0.4	,-0.5},
		{3.795	,0.35	,-0.5},
		{3.825	,0.3	,-0.5},
		{3.85	,0.25	,-0.5},
		{3.87	,0.2	,-0.5},
		{3.885	,0.15	,-0.5},
		{3.895	,0.1	,-0.5},
		{3.9	,0.05	,-0.5},
		{3.9	,0	,-0.5},
		};
		testSubsystem.setProfile(Follower.DISTANCE, profile);
		testSubsystem.resume(Follower.DISTANCE);

		while(testSubsystem.isRunning(Follower.DISTANCE)) delay(1);
		System.exit(0);
	}
	
	double position = 0;

	@Override
	public void pidWrite(double[] output) {
		System.out.println(output[0] + "\t" + position);
		position += output[0] * 1.5 * 0.1;
	}

	@Override
	public double[] pidRead() {
		return new double[] {position, 0};
	}
	
	public static void delay(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
