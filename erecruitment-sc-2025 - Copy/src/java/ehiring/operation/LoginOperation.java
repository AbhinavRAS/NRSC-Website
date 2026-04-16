package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.PasswordUtils;
import ehiring.dao.Registration;
import ehiring.db.DBConnectionManager;
import ehiring.properties.LoadProperties;

public class LoginOperation {
	public Connection conn = null;
	public DBConnectionManager conMgr = null;
	public String lPoolName = "recruit";

	LogManager logMgr;
	String emailLogId = new String();

	public LoginOperation(String emailLogId) {
		conMgr = DBConnectionManager.getInstance();
		logMgr = LogManager.getInstance(emailLogId);
		this.emailLogId = emailLogId;
	}

	public HashMap<String, String> login(Registration reg) {
		String value = new String();

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ResultSet rs_inner = null;
		PreparedStatement pstmt_inner = null;
		HashMap<String, String> map = new HashMap<String, String>();

		int i = 0;

		try {
			logMgr.accessLog("Inside login method of Login Operation");
			conn = conMgr.getConnection(lPoolName);
			// logMgr.accessLog("CON IS >> ..."+conn);
			SignUpOperation sg = new SignUpOperation(emailLogId);
			String action = sg.checkOTP(reg);
			logMgr.accessLog("ACTION IS :" + action);
			map.put("typeOfUser", action);
			if (action.equals("error"))
				return map;

			String password = reg.getPassword();
			PasswordUtils pu = new PasswordUtils();
			String encryPassWd = pu.ProtectUserPassword(password);

			pstmt = conn.prepareStatement("select * from registration where lower(email)=? AND password=? and status!='dummy'");
			pstmt.setString(1, reg.getEmail().toLowerCase());
			pstmt.setString(2, encryPassWd);

			System.out.println(
					CurrentDateTime.dateTime() + ":" + "Login QUERY IS ... changedS..>>> :" + pstmt.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) { // rs.next() && !action.equals("error")
				String status = rs.getString("status");
				// logMgr.accessLog("THE STATUS IS
				// ..."+status);
				if (status.equalsIgnoreCase("inactive"))
					map.put("typeOfUser", "inactive");
				else {

					int fail_attempt = rs.getInt("fail_login_attempts");
					System.out.println(
							CurrentDateTime.dateTime() + ":" + "attempt is ........................>" + fail_attempt);
					if (fail_attempt > 3) {
						Date date = rs.getTimestamp("fail_login_date");
						System.out.println(CurrentDateTime.dateTime() + ":"
								+ "dt is .>" + date.toString());
						boolean diff = getDiffTime(date);
						// find the difference of times

						if (diff) {
							boolean flg = new SignUpOperation(emailLogId).setLoginAttempts(reg, 0);
							if (flg) {
								map.put("typeOfUser", rs.getString("user_type"));
								map.put("dob", rs.getString("dob"));
								map.put("userName", rs.getString("name"));
								map.put("email", rs.getString("email"));
								map.put("mobile", rs.getString("contact_no"));
							}
							pstmt_inner.close();
						} else {
							map.put("typeOfUser", "attempt");
						}
					} else {
						map.put("typeOfUser", rs.getString("user_type"));
						map.put("dob", rs.getString("dob"));
						map.put("userName", rs.getString("name"));
						map.put("email", rs.getString("email"));
						map.put("mobile", rs.getString("contact_no"));
					}
				}
			} else {

				Date date_attempt = new Date();
				int attempts = 0;
				pstmt_inner = conn.prepareStatement(
						"select fail_login_attempts,fail_login_date from registration where lower(email)=? ");
				pstmt_inner.setString(1, reg.getEmail().toLowerCase());
				logMgr.accessLog("Login attempt QUERY IS .....>>> :" + pstmt_inner.toString());
				rs_inner = pstmt_inner.executeQuery();
				while (rs_inner.next()) {
					attempts = rs_inner.getInt("fail_login_attempts");
					date_attempt = rs_inner.getDate("fail_login_date");
					logMgr.accessLog("date_attempt...:" + date_attempt);
				}

				rs_inner.close();
				pstmt_inner.close();

				attempts = attempts + 1;
				boolean flg = new SignUpOperation(emailLogId).setLoginAttempts(reg, attempts);
				/*
                                if (attempts > 3)
					map.put("typeOfUser", "attempt");
                                else if(action.equals("error"))
                                        map.put("typeOfUser", "error");
                                else
					map.put("typeOfUser", "invalid");
                                */
                                if (attempts > 3)
					map.put("typeOfUser", "attempt");
                                else
					map.put("typeOfUser", "invalid");
				pstmt_inner.close();
			}

			// map.put("typeOfUser", "invalid");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

				conMgr.freeConnection(lPoolName, conn);
			} catch (SQLException e) {
			}
		}
		return map;
	}

	public String activateUser(String userId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int i = 0;
		String action = "";

		try {

			logMgr.accessLog("Inside activation method:" + userId);
			conn = conMgr.getConnection(lPoolName);

			pstmt = conn.prepareStatement("update registration set status = ?  where lower(email) = ?");
			pstmt.setString(1, "active");
			pstmt.setString(2, userId.toLowerCase());
			logMgr.accessLog("activation qry is:" + pstmt.toString());

			i = pstmt.executeUpdate();
			pstmt.close();
			// conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			conn.close();
			action = "error Could not activate due to server issue";
		} finally {
			try {
				pstmt.close();
				conMgr.freeConnection(lPoolName, conn);
			} catch (SQLException e) {
			}
		}
		if (i > 0) {
			System.out.println(CurrentDateTime.dateTime() + ":"
					+ "Account has been activated. Please enter your email id and password to login.");
			action = "success";
		} else {
			logMgr.accessLog("Account Activation  exception");
			// response.sendRedirect("administrator_profile.html");

			action = "error Invalid Link. Account could not be activated";
		}
		return action;
	}

	
	public boolean getDiffTime(Date dateStart) {

		// String dateStart = "01/14/2012 09:29:58.6666";
		// String dateStop = "01/15/2012 10:31:48.23";

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssssss");

		Date d1 = null;
		Date d2 = null;

		Calendar calendar = Calendar.getInstance(); // gets current instance of the calendar

		// logMgr.accessLog(df.format(dateNow));
		String dateStop = df.format(calendar.getTime());
		logMgr.accessLog("datestop......>>: " + dateStop);
//         /2019-11-25 16:36:46.937492
		long diffSeconds = 0;
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		boolean flg = false;

		try {
			d1 = dateStart;
			d2 = df.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");

			if (diffHours >= 1)
				flg = true;
			else if (diffMinutes >= 10)
				flg = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		logMgr.accessLog("DIFF in minues:" + diffMinutes);
		logMgr.accessLog("DIFF in diffHours:" + diffHours);
		return flg;

	}

	public boolean getDiffTimeOTP(Date dateStart) {

		// String dateStart = "01/14/2012 09:29:58.6666";
		// String dateStop = "01/15/2012 10:31:48.23";

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssssss");

		Date d1 = null;
		Date d2 = null;

		Calendar calendar = Calendar.getInstance(); // gets current instance of the calendar

		// logMgr.accessLog(df.format(dateNow));
		String dateStop = df.format(calendar.getTime());
		logMgr.accessLog("datestop......>>: " + dateStop);
//	         /2019-11-25 16:36:46.937492
		long diffSeconds = 0;
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		boolean flg = false;

		try {
			d1 = dateStart;
			d2 = df.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);

			/*
			 * System.out.print(diffDays + " days, "); System.out.print(diffHours +
			 * " hours, "); System.out.print(diffMinutes + " minutes, ");
			 * System.out.print(diffSeconds + " seconds.");
			 */

			LoadProperties lp = new LoadProperties();
			int otp_duration = Integer.parseInt(lp.getProperty("OTP_DURATION"));
			logMgr.accessLog("OTP DURATION:" + otp_duration);

			if (diffMinutes <= otp_duration && diffHours == 0)
				flg = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		logMgr.accessLog("DIFF in minues:" + diffMinutes);
		logMgr.accessLog("DIFF in diffHours:" + diffHours);
		return flg;

	}

}