package lib.frc1747.instrumentation;

public abstract class LoggedValue {
	private String name;
	private String logger;
	private boolean toSD;
	private boolean toFile;
	
	public LoggedValue(String name, String logger, boolean logToSD, boolean logToFile) {
		this.name = name;
		this.logger = logger;
		this.toSD = logToSD;
		this.toFile = logToFile;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLogger() {
		return logger;
	}
	
	public boolean willLogToSD() {
		return toSD;
	}
	
	public boolean willLogToFile() {
		return toFile;
	}
}
