package ehiring.dao;

import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.util.Date;

public class LogManager {
	static private LogManager instance; // The single instance
	static private int clients;

	private PrintWriter accessLog;
	private PrintWriter errorLog;

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the single instance, creating one if it's the first time this method
	 * is called.
	 * 
	 * @return LogManager The single instance.
	 */
	static synchronized public LogManager getInstance(String userId) {

		if (instance == null)
			instance = new LogManager(userId);
		clients++;
		return instance;
	}

	/**
	 * A private constructor since this is a Singleton
	 */
	private LogManager(String userId) {
		try {
			InetAddress ip;
			String hostname;
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
			this.setUserId(ip+","+userId.toLowerCase());
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		InputStream is = getClass().getResourceAsStream("/ehiring.properties");
		Properties dbProps = new Properties();
		try {
			dbProps.load(is);
		} catch (Exception e) {
			System.err.println(
					"Can't read the properties file. " + "Make sure Application.properties is in the CLASSPATH");
			return;
		}

		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MMM-yyyy");
		String stringDate = formatter.format(date);
		String accfileName = "Erecruit" + stringDate + ".txt";
		String accessLogFile = dbProps.getProperty("LOGFILE") + "//" + accfileName;
		try {
			accessLog = new PrintWriter(new FileWriter(accessLogFile, true), true);
			//errorLog = new PrintWriter(new FileWriter(errorLogFile, true), true);
		} catch (IOException e) {
			System.err.println("Can't open the log file: " + e.getMessage());
			accessLog = new PrintWriter(System.err);
		}

	}

	/**
	 * Writes a message to the error log file.
	 */
	public void errorLog(String msg) {
		errorLog.println(this.getUserId()+ "::" + new Date() + ": " + msg);
	}

	/**
	 * Writes a message with an Exception to the error log file.
	 */
	public void errorLog(Throwable e, String msg) {
		errorLog.println(this.getUserId() + "::" + new Date() + ": " + msg);
		e.printStackTrace(errorLog);
	}

	/**
	 * Writes a message to the access log file.
	 */
	public void accessLog(String msg) {
		//System.out.println("........userId:"+this.getUserId() );
		accessLog.println(this.getUserId() + "::" + new Date() + ": " + msg);
	}
}
