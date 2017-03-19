package lib.frc1747.instrumentation;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger {
	private String name;
	private Level level;
	private Instrumentation instrumentation;
	
	protected Logger(String name, Instrumentation instrumentation) {
		this.name = name;
		level = Level.INFO;
		this.instrumentation = instrumentation;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	
	public void log(Level level, String message, Object... args) {
		if(level.intValue() >= this.level.intValue()) {
			LogRecord record = new LogRecord(level, String.format(message, args));
			record.setLoggerName(name);
			record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
			instrumentation.addMessage(record);
		}
	}
	
	public void exception(Throwable thrown, String message, Object... args) {
		LogRecord record = new LogRecord(Level.SEVERE, String.format(message, args));
		record.setLoggerName(name);
		record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
		record.setThrown(thrown);
		instrumentation.addMessage(record);
	}
}
