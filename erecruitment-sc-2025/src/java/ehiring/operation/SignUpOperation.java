package ehiring.operation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.PasswordUtils;
import ehiring.dao.Registration;
import ehiring.db.DBConnectionManager;

public class SignUpOperation {

    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    LogManager logMgr;
    String emailLogId = new String();

    public SignUpOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
        this.emailLogId = emailLogId;
    }

    public int checkMailMobile(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        int i = 0;
        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside check mobile method");
            con = conMgr.getConnection(lPoolName);

            String password = reg.getPassword();
            PasswordUtils pu = new PasswordUtils();

            pstmt = con.prepareStatement("select count(*) from registration where lower(email)=? and status!='dummy'");
            pstmt.setString(1, reg.getEmail().toLowerCase());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Account already exists!!");
                    return -1;
                }
            }
            rs.close();
            pstmt.close();

            pstmt = con.prepareStatement("select count(*) from registration where contact_no=? and status!='dummy' ");
            pstmt.setLong(1, reg.getMobile());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Mobile no already exists!!");
                    return -2;
                }
            }

            /*
             * pstmt = con.
             * prepareStatement("delete from registration where (contact_no=? or email=?) and status=?"
             * ); pstmt.setLong(1, reg.getMobile()); pstmt.setString(2, reg.getEmail());
             * pstmt.setString(3, "dummy"); logMgr.accessLog( "delete pstmt:" +
             * pstmt.toString()); int ret = pstmt.executeUpdate(); if (ret < 0) { return -3;
             * }
             */
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }

        return i;
    }

    public int register(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        int i = 0;
        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside Registration method");
            con = conMgr.getConnection(lPoolName);

            String password = reg.getPassword();
            PasswordUtils pu = new PasswordUtils();
            String encryPassWd = pu.ProtectUserPassword(password);

            pstmt = con
                    .prepareStatement("delete from registration where (contact_no=? or lower(email)=?) and status=?");
            pstmt.setLong(1, reg.getMobile());
            pstmt.setString(2, reg.getEmail().toLowerCase());
            pstmt.setString(3, "dummy");
            logMgr.accessLog("delete pstmt:" + pstmt.toString());
            int ret = pstmt.executeUpdate();
            if (ret < 0) {
                return -3;
            }

            pstmt.close();

            pstmt = con.prepareStatement("select count(*) from registration where lower(email)=? and status!='dummy'");
			// pstmt.setString(1, reg.getName());
            // pstmt.setDate(2, reg.getDob());
            pstmt.setString(1, reg.getEmail().toLowerCase());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Account already exists!!");
                    return -1;
                }
            }
            rs.close();
            pstmt.close();

            pstmt = con.prepareStatement("select count(*) from registration where contact_no=? and status!='dummy' ");
            pstmt.setLong(1, reg.getMobile());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Mobile already exists!!");
                    return -2;
                }
            }

            pstmt = con.prepareStatement(
                    "insert into registration(name,dob,email,password,user_type,status,contact_no) Values(?,?,?,?,?,?,?)");
            logMgr.accessLog("insert pstmt:" + pstmt.toString());
            pstmt.setString(1, reg.getName());
            pstmt.setDate(2, reg.getDob());
            pstmt.setString(3, reg.getEmail());
            pstmt.setString(4, encryPassWd);
            pstmt.setString(5, reg.getUserType());
            pstmt.setString(6, "inactive");
            pstmt.setLong(7, reg.getMobile());

            i = pstmt.executeUpdate();

            pstmt.close();
            // con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }

        return i;
    }

    public String updatePasswd(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside updatePasswd method");
            con = conMgr.getConnection(lPoolName);

            String password = reg.getPassword();
            PasswordUtils pu = new PasswordUtils();
            String encryPassWd = pu.ProtectUserPassword(password);
            
            // Start: ChangeId: 2023120604
            pstmt = con.prepareStatement(
                    "select * from registration where lower(email)=? ");
            pstmt.setString(1, reg.getEmail().toLowerCase());

            rs = pstmt.executeQuery();
            String current_password = "";
            while (rs.next()) {
                current_password = rs.getString("password");
                if(current_password.equals(encryPassWd)){
                    logMgr.accessLog("updatePasswd: New password cannot be same as the last one!");
                    return "error_samepassword";
                }
            }
            rs.close();
            pstmt.close();
            // End: ChangeId: 2023120604

            pstmt = con.prepareStatement("update registration set password = ?  where lower(email) = ?");
            pstmt.setString(1, encryPassWd);
            pstmt.setString(2, reg.getEmail().toLowerCase());
            logMgr.accessLog("update mpobile..:" + pstmt.toString());

            i = pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            con.close();
            action = "error";
        } finally {
            try {
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        if (i > 0) {
            logMgr.accessLog("Updated password Successfully");
            action = "success";
        } else {
            logMgr.accessLog("Updated password  exception");
            // response.sendRedirect("administrator_profile.html");

            action = "error";
        }
        return action;
    }

    public boolean setLoginAttempts(Registration reg, int attempts) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        boolean flg = false;

        try {
            conn = conMgr.getConnection(lPoolName);
            String qry = "update registration set fail_login_attempts=? where lower(email)=? and status!='dummy'";
            if (attempts == 1) {
                qry = "update registration set fail_login_attempts=?, fail_login_date=localtimestamp where lower(email)=? and status!='dummy'";
            }

            pstmt = conn.prepareStatement(qry);
            pstmt.setInt(1, attempts);
            pstmt.setString(2, reg.getEmail().toLowerCase());

            logMgr.accessLog("Login QUERY IS update.....>>> :" + pstmt.toString());
            int retn = pstmt.executeUpdate();
            if (retn < 0) {
                System.out.println(
                        CurrentDateTime.dateTime() + ":" + "Login QUERY IS .....>>> : could not update attempts");
                flg = false;
            } else {
                flg = true;
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();

            flg = false;
        }
        return flg;

    }

    public HashMap checkAttempts(Registration reg) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";
        HashMap mp = new HashMap();

        try {
            conn = conMgr.getConnection(lPoolName);
            java.util.Date date_attempt = new java.util.Date();
            int attempts = 0;
            pstmt = conn.prepareStatement(
                    "select fail_login_attempts,fail_login_date from registration where lower(email)=? ");
            pstmt.setString(1, reg.getEmail().toLowerCase());
            System.out
                    .println(CurrentDateTime.dateTime() + ":" + "Login attempt QUERY IS .....>>> :" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                attempts = rs.getInt("fail_login_attempts");
                date_attempt = rs.getTimestamp("fail_login_date");
                logMgr.accessLog("date_attempt...:" + date_attempt);
            }

            rs.close();
            pstmt.close();
            if (attempts > 3) {
                System.out.println(CurrentDateTime.dateTime() + ":"
                        + "dt is .................2019-11-25 16:33:14.192902......>" + date_attempt.toString());
                boolean diff = new LoginOperation(emailLogId).getDiffTime(date_attempt);
                if (diff) {
                    mp.put("attempt", "allow");
                    setLoginAttempts(reg, 0);
                } else {
                    mp.put("attempt", "locked");
                }

            } else {
                mp.put("attempt", "increased");
                attempts = attempts + 1;
                setLoginAttempts(reg, attempts);

            }

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            action = "error";
        }
        System.out.println("THE VALUE IS:" + action);
        return mp;

    }

    public String checkPasswd(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside checkPasswd method:");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select count(*) from registration where password=? and lower(email)=?");
            pstmt.setString(1, reg.getOldPassword());
            pstmt.setString(2, reg.getEmail().toLowerCase());
            logMgr.accessLog("SECURE qry ID :" + pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Password Matches ");
                    // return "success";
                    action = "success";
                } else {
                    action = "error";
                }

            } else {
                action = "error";
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            con.close();
            action = "error";
        } finally {
            try {

                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        System.out.println("THE VALUE IS:" + action);
        return action;
    }

    public boolean OTPValidity(Registration reg) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        boolean flg = false;

        try {
            conn = conMgr.getConnection(lPoolName);
            java.util.Date date_attempt = new java.util.Date();
            int attempts = 0;
            pstmt = conn.prepareStatement("select otp,otp_time from registration where lower(email)=? ");
            pstmt.setString(1, reg.getEmail().toLowerCase());
            System.out
                    .println(CurrentDateTime.dateTime() + ":" + "Login attempt QUERY IS .....>>> :" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                date_attempt = rs.getTimestamp("otp_time");
                logMgr.accessLog("date_attempt...:" + date_attempt);
            }

            rs.close();
            pstmt.close();
            System.out.println(CurrentDateTime.dateTime() + ":"
                    + "dt is .................2019-11-25 16:33:14.192902......>" + date_attempt.toString());
            boolean diff = new LoginOperation(emailLogId).getDiffTimeOTP(date_attempt);
            if (diff) {
                flg = true;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flg;
    }

    public String checkOTP(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "error";
        String OTP = new String();

        PasswordUtils pu = new PasswordUtils();

        try {

            reg.setUserType("applicant");
            if (!OTPValidity(reg)) {
                action = "error_expired";
            } else {
                logMgr.accessLog("Inside checkOTP method");
                con = conMgr.getConnection(lPoolName);

                pstmt = con.prepareStatement("select count(*) from registration where otp=? and lower(email)=?");
                pstmt.setString(1, reg.getOTP());
                pstmt.setString(2, reg.getEmail().toLowerCase());
                logMgr.accessLog("check otp:" + pstmt.toString());

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        logMgr.accessLog("OTP Matches ");
                        // return "success";
                        action = "success";
                    }
                } else {
                    action = "error";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
            action = "error";
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return action;
    }

    public String getCurrentOTP(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String otp = "-1";

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside checkOTP method");
            con = conMgr.getConnection(lPoolName);

            pstmt = con.prepareStatement("select otp from registration where lower(email)=?");
            pstmt.setString(1, reg.getEmail().toLowerCase());
            logMgr.accessLog("current otp is :" + pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                otp = rs.getString("otp");
                // return "success";

            }
            logMgr.accessLog("current otp is :" + otp);
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        logMgr.accessLog("current otp is finally:" + otp);
        return otp;
    }

    public long getMobileNo(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        long mobile = 0;

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside checkOTP method");
            con = conMgr.getConnection(lPoolName);
            System.out.println("con:" + conMgr);
            System.out.println("con:" + lPoolName);
            System.out.println("con:" + con);
            pstmt = con.prepareStatement("select contact_no from registration where lower(email)=?");
            pstmt.setString(1, reg.getEmail().toLowerCase());
//			pstmt.setString(1, "richagoyal@nrsc.gov.in");
            System.out.println("check qry:" + pstmt.toString());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                mobile = Long.parseLong(rs.getString("contact_no"));
                // return "success";

            }
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return mobile;
    }

    public String insertOTP(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside insertOTP method");
            con = conMgr.getConnection(lPoolName);

            pstmt = con
                    .prepareStatement("update registration set otp = ?,otp_time=localtimestamp  where lower(email)=?");
            pstmt.setString(1, reg.getOTP());
            pstmt.setString(2, reg.getEmail().toLowerCase());
            logMgr.accessLog("Inserted OTP stmt:" + pstmt.toString());

            i = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
            action = "error";
        }

        if (i > 0) {
            logMgr.accessLog("Inserted OTP Successfully");
            action = "success";
        } else {
            logMgr.accessLog("Inserted OTP exception");
            // response.sendRedirect("administrator_profile.html");

            action = "error";
        }
        return action;
    }

    public String isMobileNoExists(Registration reg) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "success";
        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside ismobile exists method");
            con = conMgr.getConnection(lPoolName);
            long mobileNo = getMobileNo(reg);
            if (mobileNo == reg.getMobile()) {
                action = "same";
            } else {
                pstmt = con.prepareStatement("select count(*) from registration where contact_no=? ");
                pstmt.setLong(1, reg.getMobile());

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        logMgr.accessLog("Mobile already exists!!");
                        action = "exists";
                    }
                }

                rs.close();
                pstmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
            action = "error";
        }

        return action;
    }

    public String updateMobile(Registration reg) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";
        long oldMobile = reg.getMobile();

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside updateMobile method");
            con = conMgr.getConnection(lPoolName);
            long mobileNo = getMobileNo(reg);
            if (mobileNo == reg.getMobile()) {
                action = "same";
            } else {
                pstmt = con.prepareStatement("select count(*) from registration where contact_no=? ");
                pstmt.setLong(1, reg.getMobile());

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        logMgr.accessLog("Mobile already exists!!");
                        action = "exists";
                    }
                }

                rs.close();
                pstmt.close();

                if (!action.equalsIgnoreCase("exists")) {
                    pstmt = con.prepareStatement("update registration set contact_no = ?  where lower(email)=?");
                    pstmt.setLong(1, reg.getMobile());
                    pstmt.setString(2, reg.getEmail().toLowerCase());
                    logMgr.accessLog("update Mobile:" + pstmt.toString());

                    i = pstmt.executeUpdate();
                    pstmt.close();
                    // con.close();
                    if (i > 0) {
                        logMgr.accessLog("New Mobile Successfully changed");
                        action = "success";
                    } else {
                        logMgr.accessLog("Mobile updation  exception");
                        // response.sendRedirect("administrator_profile.html");

                        action = "error";
                    }

                    if (action.equalsIgnoreCase("success")) {
                        pstmt = con.prepareStatement("insert into updateMobileLog (email,contact_no) Values(?,?)");
                        pstmt.setLong(2, reg.getMobile());
                        pstmt.setString(1, reg.getEmail().toLowerCase());
                        logMgr.accessLog("insert old Mobile:" + pstmt.toString());

                        i = pstmt.executeUpdate();
                        pstmt.close();
                        con.close();
                        if (i > 0) {
                            logMgr.accessLog("old Mobile Successfully inserted to log");
                            action = "success";
                        } else {
                            logMgr.accessLog("Old Mobile insertion  exception");
                            // response.sendRedirect("administrator_profile.html");

                            action = "error";
                        }
                    }
                    if (action.equalsIgnoreCase("success")) {
                        reg.setOTP("");
                        String action_Str = new SignUpOperation(emailLogId).insertOTP(reg);
                        if (action_Str.equalsIgnoreCase("error")) {
                            action = action_Str;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
            action = "error";
        }

        return action;
    }

    public String insertSignupOTP(Registration reg) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        String action = "";
        Date dummyDate = new Date(System.currentTimeMillis());

        try {
            reg.setUserType("applicant");
            logMgr.accessLog("Inside Registration method");
            con = conMgr.getConnection(lPoolName);

            pstmt = con
                    .prepareStatement("delete from registration where (contact_no=? or lower(email)=?) and status=?");
            pstmt.setLong(1, reg.getMobile());
            pstmt.setString(2, reg.getEmail().toLowerCase());
            pstmt.setString(3, "dummy");
            logMgr.accessLog("delete pstmt:" + pstmt.toString());
            int ret = pstmt.executeUpdate();
            if (ret < 0) {
                return "error";
            }
            pstmt.close();

            /*
             * pstmt = con.prepareStatement(
             * "insert into registration(name,dob,email,password,user_type,status,contact_no,otp,otp_time) Values(?,?,?,?,?,?,?,?,?)"
             * ); pstmt.setString(1, "dummy"); pstmt.setDate(2, dummyDate);
             * pstmt.setString(3, reg.getEmail()); pstmt.setString(4, "dummy");
             * pstmt.setString(5, "applicant"); pstmt.setString(6, "dummy");
             * pstmt.setLong(7, reg.getMobile()); pstmt.setString(8, reg.getOTP());
             * pstmt.setDate(9, date_of_otp_update);
             */
            String stmtQry = "insert into registration(name,dob,email,password,user_type,status,contact_no,otp,otp_time) Values('dummy','"
                    + dummyDate + "','" + reg.getEmail() + "','dummy','applicant','dummy'," + reg.getMobile() + ","
                    + reg.getOTP() + ",localtimestamp)";
            pstmt = con.prepareStatement(stmtQry);

            logMgr.accessLog(".........>" + pstmt.toString());
            i = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // con.close();
            action = "error";
        }

        if (i > 0) {
            logMgr.accessLog("New OTP Successfully Entered");
            action = "success";
        } else {
            logMgr.accessLog("New OTP Successfully Entered");
            // response.sendRedirect("administrator_profile.html");

            action = "error";
        }
        return action;
    }

}
