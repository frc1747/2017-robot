package lib.frc1747.instrumentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Contains most of the backend work of the logging framework.
 * @author Tiger Huang
 */
public class Instrumentation implements Thread.UncaughtExceptionHandler {
	// Singleton instance of this class
	private static Instrumentation instance;
	
	// Logging directory location
	//private static final String logDir = "C:/Users/Tiger/Documents/logs";
	private static final String logDir = "/home/lvuser/logs";
	
	// Message log format
	private static final String format = "[%1$+d]{%2$d} %3$s\n" + "%4$s" + "%5$s: %6$s\n" + "%7$s\n";
	
	// Logfiles
	private PrintWriter messageWriter;
	private PrintWriter valueWriter;
	
	// Timer to run async operations
	private Timer timer;
	
	// Logger sources
	private Vector<Logger> loggers;
	
	// One time messages to write to the log
	private ConcurrentLinkedQueue<LogRecord> messages;
	
	// Constantly logged data
	private Vector<LoggedValue> values;
	
	// Offset times based on when logging starts
	private long startTime;
	
	// Logger for messages inside instrumentation
	private Logger logger;
	
	/**
	 * Gets the singleton instance of instrumentation. Creates it if it does not exist yet.
	 * @return the single instance of instrumentation
	 */
	protected static Instrumentation getInstance() {
		if(instance == null) {
			instance = new Instrumentation();
		}
		return instance;
	}
	
	/**
	 * Flushes all output buffers
	 */
	protected void flushAll() {
		System.out.flush();
		if(messageWriter != null) messageWriter.flush();
		if(valueWriter != null) valueWriter.flush();
	}
	
	/**
	 * Adds a message to the queue. Normal users should not be directly adding
	 * messages to the queue.
	 * @param message the message to be logged
	 */
	protected void addMessage(LogRecord message) {
		messages.add(message);
	}
	
	/**
	 * Singleton constructor for instrumentation
	 */
	private Instrumentation() {
		// Init lists
		timer = new Timer();
		messages = new ConcurrentLinkedQueue<>();
		loggers = new Vector<>();
		values = new Vector<>();
		
		// Set default exception handler
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		// Init self logger
		logger = getLoggerInternal("Instrumentation");
		logger.setLevel(Level.INFO);
		
		// Give other code 1 second to initialize
		timer.schedule(new DelayedInit(), 10 * 1000);
	}
	
	/**
	 * Returns the logger with the specified name, or creates it if
	 * a logger with the specified name does not exist yet.
	 * @param name the name of the logger
	 * @return a logger with the specified name
	 */
	private Logger getLoggerInternal(String name) {
		// Return existing logger if it exists
		for(Logger logger:loggers) {
			if(logger.getName().equals(name)) {
				return logger;
			}
		}
		
		// Return new logger if necessary
		Logger logger = new Logger(name, this);
		loggers.add(logger);
		return logger;
	}

	/**
	 * Returns the logger with the specified name, or creates it if
	 * a logger with the specified name does not exist yet.
	 * @param name the name of the logger
	 * @return a logger with the specified name
	 */
	public static Logger getLogger(String name) {
		return Instrumentation.getInstance().getLoggerInternal(name);
	}
	
	/**
	 * Runs at a specified time after code start in order to allow all value writers to register
	 * @author Tiger Huang
	 */
	private class DelayedInit extends TimerTask {
		@Override
		public void run() {
			startTime = System.currentTimeMillis();

			// Get a unique filename and timestamp
			Date date = new Date();
			String dateString = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
			
			// Log start information
			logger.log(Level.INFO, "Logging started at %s", date);

			// Attempt to initialize message log file
			String messageFileName = logDir + "/" + dateString + ".log";
			try {
				messageWriter = new PrintWriter(
						new BufferedWriter(
						new FileWriter(messageFileName)));
			}
			catch (IOException ex) {
				logger.exception(ex, "Logging messages to a file will be disabled for this session.");
				messageWriter = null;
			}
			if(messageWriter != null) {
				logger.log(Level.INFO, "Logging messages to file: %s", messageFileName);
			}

			// Attempt to initialize value log file
			String valueFileName = logDir + "/" + dateString + ".csv";
			try {
				valueWriter = new PrintWriter(
						new BufferedWriter(
						new FileWriter(valueFileName)));
			}
			catch (IOException ex) {
				logger.exception(ex, "Logging values to a file will be disabled for this session.");
				valueWriter = null;
			}
			if(valueWriter != null) {
				logger.log(Level.INFO, "Logging values to file: %s", valueFileName);
			}
			
			// Add all logged values
			for(Logger logger:loggers) {
				values.addAll(logger.getLoggedValues().values());
			}
			
			// Make headers
			if(valueWriter != null) {
				// Which logger
				for(LoggedValue value:values) {
					if(value.willLogToFile()) {
						valueWriter.print(value.getLogger() + ", ");
					}
				}
				valueWriter.println();
				
				// The value name
				for(LoggedValue value:values) {
					if(value.willLogToFile()) {
						valueWriter.print(value.getName() + ", ");
					}
				}
				valueWriter.println();
			}

			// Actually start other tasks
			timer.scheduleAtFixedRate(new MessagePeriodic(), 0, 100);
			timer.scheduleAtFixedRate(new ValueSDPeriodic(), 0, 100);
			timer.scheduleAtFixedRate(new ValueFilePeriodic(), 0, 10);
		}
	}
	
	/**
	 * Runs at a periodic rate to actually save the messages
	 * @author Tiger Huang
	 */
	private class MessagePeriodic extends TimerTask {
		@Override
		public void run() {
			// Process all queued messages
			while(!messages.isEmpty()) {
				// Get the record
				LogRecord record = messages.poll();
				
				// Handle always present elements
				long time = record.getMillis() - startTime;
				int thread = record.getThreadID();
				Level level = record.getLevel();
				String message = record.getMessage();
				
				// Handle elements that may or may not be present
				String loggerName = "";
				if(record.getLoggerName() != null) {
					loggerName = "from " + record.getLoggerName();
				}
				
				String source = "";
				if(record.getSourceClassName() != null) {
					source = "at " + record.getSourceClassName() + "\n";
				}
				
				String thrown = "";
				if(record.getThrown() != null) {
					StringWriter errors = new StringWriter();
					record.getThrown().printStackTrace(new PrintWriter(errors));
					thrown = errors.toString();
				}
				
				// Actually create the message
				String output = String.format(format,
						time, thread, loggerName, source,
						level, message, thrown);
				
				// Output the message
				System.out.print(output);
				if(messageWriter != null) {
					messageWriter.print(output);
				}
			}
		}
	}

	/**
	 * Writes logged values to the smart dashboard
	 * @author Tiger Huang
	 */
	private class ValueSDPeriodic extends TimerTask {
		@Override
		public void run() {
			for(LoggedValue value:values) {
				if(value.willLogToSD()) {
					if(value instanceof LoggedDouble)
						SmartDashboard.putNumber(value.getLogger() + ": " + value.getName(), ((LoggedDouble)value).value);
					if(value instanceof LoggedString)
						SmartDashboard.putString(value.getLogger() + ": " + value.getName(), ((LoggedString)value).value);
					if(value instanceof LoggedBoolean)
						SmartDashboard.putBoolean(value.getLogger() + ": " + value.getName(), ((LoggedBoolean)value).value);
					if(value instanceof LoggedSendable)
						SmartDashboard.putData(value.getLogger() + ": " + value.getName(), ((LoggedSendable)value).value);
				}
			}
		}
	}
	
	/**
	 * Writes logged values to a file at a periodically
	 * @author Tiger Huang
	 */
	private class ValueFilePeriodic extends TimerTask {
		@Override
		public void run() {
			if(valueWriter != null) {
				System.out.println(values.size());
				for(LoggedValue value:values) {
					if(value.willLogToFile()) {
						valueWriter.print(value.toString() + ", ");
					}
				}
				valueWriter.println();
			}
			
			flushAll();
		}	
	}

	/**
	 * Method to handle catching all uncaught exceptions for logging purposes
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LogRecord record = new LogRecord(Level.SEVERE, "Uncaught Exception");
		record.setThreadID((int)t.getId());
		record.setThrown(e);
		messages.add(record);
	}
}
