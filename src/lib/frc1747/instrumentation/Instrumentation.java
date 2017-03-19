package lib.frc1747.instrumentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Instrumentation implements Thread.UncaughtExceptionHandler {
	private static Instrumentation instance;
	
	// Logging directory location
	private static final String logDir = "C:/Users/Tiger/Documents/logs";
	
	// Message log format
	private static final String format = "[%1$+d]{%2$d} from %3$s\n" + "at %4$s\n" + "%5$s: %6$s\n" + "%7$s\n";
	
	// Timer to run async operations
	private Timer timer;
	
	// Logger sources
	private Vector<Logger> loggers;
	
	// One time messages to write to the log
	private ConcurrentLinkedQueue<LogRecord> messages;
	
	// Constantly logged data
	private ArrayList<LoggedValue> values;
	
	// Logfiles
	private PrintWriter messageWriter;
	private PrintWriter valueWriter;
	
	// Offset times based on when logging starts
	private long startTime;
	
	// Logger for messages inside instrumentation
	private Logger logger;
	
	private Instrumentation() {
		timer = new Timer();
		messages = new ConcurrentLinkedQueue<>();
		loggers = new Vector<>();
		values = new ArrayList<>();
		
		// Set default exception handler
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		// Init self logger
		logger = getLoggerInternal("Instrumentation");
		
		// Give other code 1 second to initialize
		timer.schedule(new DelayedInit(), 1 * 1000);
	}
	
	protected void addMessage(LogRecord message) {
		messages.add(message);
	}
	
	public void flushAll() {
		if(messageWriter != null) messageWriter.flush();
		if(valueWriter != null) valueWriter.flush();
	}
	
	public static Instrumentation getInstance() {
		if(instance == null) {
			instance = new Instrumentation();
		}
		return instance;
	}
	
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
	
	public static Logger getLogger(String name) {
		return Instrumentation.getInstance().getLoggerInternal(name);
	}
	
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
			if(messageWriter != null) {
				logger.log(Level.INFO, "Logging values to file: %s", valueFileName);
			}

			// Actually start other tasks
			timer.scheduleAtFixedRate(new MessagePeriodic(), 0, 100);
			timer.scheduleAtFixedRate(new ValueSDPeriodic(), 0, 100);
			timer.scheduleAtFixedRate(new ValueFilePeriodic(), 0, 10);
		}
	}
	
	private class MessagePeriodic extends TimerTask {

		@Override
		public void run() {
			//messages.add(new LogRecord(Level.SEVERE, "Running"));
			// TODO Auto-generated method stub
			while(!messages.isEmpty()) {
				LogRecord record = messages.poll();
				long time = record.getMillis() - startTime;
				int thread = record.getThreadID();
				String logger = record.getLoggerName();
				String source = record.getSourceClassName();
				Level level = record.getLevel();
				String message = record.getMessage();
				String thrown = "";
				if(record.getThrown() != null) {
					StringWriter errors = new StringWriter();
					record.getThrown().printStackTrace(new PrintWriter(errors));
					thrown = errors.toString();
				}
				String output = String.format(format,
						time, thread, logger, source,
						level, message, thrown);
				
				System.out.print(output);
				if(messageWriter != null) {
					messageWriter.print(output);
				}
				
				// TODO: Remove (Testing right now)
				flushAll();
			}
		}
		
	}
	
	private class ValueSDPeriodic extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ValueFilePeriodic extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LogRecord record = new LogRecord(Level.SEVERE, "Uncaught Exception");
		// Actually contains both class name and method name
		record.setSourceClassName("");
		record.setThreadID((int)t.getId());
		record.setThrown(e);
		messages.add(record);
	}
}
