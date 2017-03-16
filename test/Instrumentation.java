public class Instrumentation {
	private static Instrumentation instance;
	
	private Instrumentation() {
		
	}
	
	public static Instrumentation getInstance() {
		if(instance == null) {
			instance = new Instrumentation();
		}
		return instance;
	}
}
