package lib.frc1747.instrumentation;

import edu.wpi.first.wpilibj.Sendable;

public class LoggedSendable extends LoggedValue {
	public Sendable value;
	
	public LoggedSendable(String name, String logger, boolean logToSD, boolean logToFile) {
		super(name, logger, logToSD, logToFile);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
