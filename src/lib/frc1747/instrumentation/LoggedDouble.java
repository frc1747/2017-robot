package lib.frc1747.instrumentation;

public class LoggedDouble extends LoggedValue {
	public double value;
	
	public LoggedDouble(String name, String logger, boolean logToSD, boolean logToFile) {
		super(name, logger, logToSD, logToFile);
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
}
