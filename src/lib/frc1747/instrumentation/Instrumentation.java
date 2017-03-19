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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Instrumentation {
	private static Instrumentation instance;
	
	// Logging directory location
	private static final String logDir = "C:/Users/Tiger/Documents/logs";
	
	// Message log format
	private static final String format = "[%1$+d]{%2$d} %3$s\n" + "%4$s: %5$s\n" + "%6$s\n";
	
	// Timer to run async operations
	private Timer timer;
	
	// One time messages to write to the log
	private ConcurrentLinkedQueue<LogRecord> messages;
	
	// Constantly logged data
	private ArrayList<LoggedValue> values;
	
	// Logfiles
	private PrintWriter messageWriter;
	private PrintWriter valueWriter;
	
	// Offset times based on when logging starts
	private long startTime;
	
	private Instrumentation() {
		timer = new Timer();
		messages = new ConcurrentLinkedQueue<>();
		values = new ArrayList<>();
		
		// Give other code 1 second to initialize
		timer.schedule(new DelayedInit(), 1 * 1000);
	}
	
	public void log(Level level, String message) {
		LogRecord record = new LogRecord(level, message);
		// Actually contains both class name and method name
		record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
		messages.add(record);
	}
	
	public void exception(Throwable thrown) {
		LogRecord record = new LogRecord(Level.SEVERE, "");
		// Actually contains both class name and method name
		record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
		record.setThrown(thrown);
		messages.add(record);
	}
	
	public void exception(Throwable thrown, String message) {
		LogRecord record = new LogRecord(Level.SEVERE, message);
		// Actually contains both class name and method name
		record.setSourceClassName(Thread.currentThread().getStackTrace()[2].toString());
		record.setThrown(thrown);
		messages.add(record);
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
	
	private class DelayedInit extends TimerTask {
		@Override
		public void run() {
			startTime = System.currentTimeMillis();

			// Get a unique filename and timestamp
			Date date = new Date();
			String dateString = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
			
			// Log start information
			log(Level.INFO, "Logging started at " + date.toString());

			// Attempt to initialize message log file
			try {
				messageWriter = new PrintWriter(
						new BufferedWriter(
						new FileWriter(logDir + "/" + dateString + ".log")));
			}
			catch (IOException ex) {
				exception(ex, "Logging messages to a file will be disabled for this session.");
				messageWriter = null;
			}
			if(messageWriter != null) {
				log(Level.INFO, "Logging messages to file: " + logDir + "/" + dateString + ".log");
			}

			// Attempt to initialize value log file
			try {
				valueWriter = new PrintWriter(
						new BufferedWriter(
						new FileWriter(logDir + "/" + dateString + ".csv")));
			}
			catch (IOException ex) {
				exception(ex, "Logging values to a file will be disabled for this session.");
				valueWriter = null;
			}
			if(messageWriter != null) {
				log(Level.INFO, "Logging values to file: " + logDir + "/" + dateString + ".csv");
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
						time, thread, source,
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
}
