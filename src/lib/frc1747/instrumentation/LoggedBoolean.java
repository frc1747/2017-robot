package lib.frc1747.instrumentation;

public class LoggedBoolean extends LoggedValue {
	public boolean value;
	
	public LoggedBoolean(String name, String logger, boolean logToSD, boolean logToFile) {
		super(name, logger, logToSD, logToFile);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}
