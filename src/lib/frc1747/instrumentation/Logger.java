package lib.frc1747.instrumentation;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Logger contains the public facing API of the logging framework.
 * @author Tiger Huang
 */
public class Logger {
	private String name;
	private Level level;
	private Instrumentation instrumentation;
	
	/**
	 * Only Instrumentation should be initializing Loggers
	 * @param name the name of the logger
	 * @param instrumentation the singleton instance of instrumentation
	 */
	protected Logger(String name, Instrumentation instrumentation) {
		this.name = name;
		level = Level.INFO;
		this.instrumentation = instrumentation;
	}
	
	/**
	 * Sets the minimum level of messages that will be logged
	 * @param level the minimum level to log
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * Gets the name of this logger
	 * @return gets the name for this logger
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Writes a message to the log.<br>
	 * See java.util.Formatter for detailed formatting information
	 * @param level the level of the message to write
	 * @param message the message to write (can use a formatting string for String.format)
	 * @param args the remaining arguments to String.format
	 */
	public void log(Level level, String message, Object... args) {
		if(level.intValue() >= this.level.intValue()) {
			LogRecord record = new LogRecord(level, String.format(message, args));
			record.setLoggerName(name);
			record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
			instrumentation.addMessage(record);
		}
	}
	
	/**
	 * Write an exception to the log.<br>
	 * See java.util.Formatter for detailed formatting information
	 * @param thrown the exception to write
	 * @param message the message to write (can use a formatting string for String.format)
	 * @param args the remaining arguments to String.format
	 */
	public void exception(Throwable thrown, String message, Object... args) {
		LogRecord record = new LogRecord(Level.SEVERE, String.format(message, args));
		record.setLoggerName(name);
		record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
		record.setThrown(thrown);
		instrumentation.addMessage(record);
	}
}
