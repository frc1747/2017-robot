package lib.frc1747.instrumentation;

public class LoggedString extends LoggedValue {
	public String value;
	
	public LoggedString(String name, String logger, boolean logToSD, boolean logToFile) {
		super(name, logger, logToSD, logToFile);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
