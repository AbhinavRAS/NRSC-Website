package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.RegistrationRelation;
import ehiring.db.DBConnectionManager;
import java.sql.Statement;

public class RegistrationRelationOperation {

    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    LogManager logMgr;

    public RegistrationRelationOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
    }

    public int establishRelationship(RegistrationRelation rr, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logMgr.accessLog("Inside Establish Relationship method....");
            logMgr.accessLog("reg relation id2......." + rr.getRegistration_id());
            pstmt = con.prepareStatement("insert into registration_relation(email,registration_id,advt_no,post_no) Values(?,?,?,?)");
            pstmt.setString(1, rr.getEmail());
            pstmt.setString(2, rr.getRegistration_id());
            pstmt.setString(3, rr.getAdvt_no());
            pstmt.setString(4, rr.getPost_no());
            logMgr.accessLog("qyer is :" + pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public String getUniqueRegistration(RegistrationRelation rr, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int id = -1;
        String regId = "-1";
        try {
            logMgr.accessLog("Inside getUniqueRegistration method....");
            System.out.println("Inside getUniqueRegistration method....");
            String insStmt = "insert into uniqueregid (advt_no,post_no,email) Values(?,?,?) returning id";

            //  pstmt = con.prepareStatement("insert into uniqueregid (advt_no,post_no,email) Values(?,?,?) returning id");
            pstmt = con.prepareStatement(insStmt, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, rr.getAdvt_no());
            pstmt.setString(2, rr.getPost_no());
            pstmt.setString(3, rr.getEmail());
            logMgr.accessLog("qyer is :" + pstmt.toString());
            int exeVal = pstmt.executeUpdate();
            System.out.println("update id result is :" + exeVal);

            if (exeVal > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                System.out.println("update id result is :" + exeVal );
                if (keys.next()) {
                    id = keys.getInt(1);
                    System.out.println("Value id  is :" + id);
                    if (id == -1) {
                        return regId;
                    } else {
                        String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
                        regId = currentYear + "-" + id;
                        rr.setRegistration_id(regId);
                    }
                }
            } else {
                id = -1;
                regId = "-1";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        logMgr.accessLog("regId is :" + regId);
        System.out.println("regId is :" + regId);
        return regId;
    }

    public int getMaxUniqueRegistration(RegistrationRelation rr, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logMgr.accessLog("Inside Establish Relationship method....");
            logMgr.accessLog("reg relation id2......." + rr.getRegistration_id());
            pstmt = con.prepareStatement("select max(id) from uniqueregid where post_no=? and advt_no=? and lower(email)=?");
            pstmt.setString(1, rr.getAdvt_no());
            pstmt.setString(2, rr.getPost_no());
            pstmt.setString(3, rr.getEmail());
            logMgr.accessLog("qyer is :" + pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public String get8DigitNumber(int max) {
        String formattedNumber = new String();
        int len = Integer.toString(max).length();
        logMgr.accessLog(len + "");
        if (len < 8) {
            formattedNumber = String.format("%08d", max);
            logMgr.accessLog("Formatted number is : " + formattedNumber);
        }
        return formattedNumber;
    }

    public String generateRegistration_id() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String registration_id = new String();

        String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        try {
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select substring(registration_id,6) from registration_relation where registration_id like '"
                    + currentYear + "-%'  order by registration_id ");
            logMgr.accessLog("reg id string:" + pstmt.toString());

            rs = pstmt.executeQuery();
            int max = 0;
            while (rs.next()) {
                String tempStr = rs.getString(1);
                if (tempStr.startsWith("NA")) {
                    continue; //save draft record
                }				//logMgr.accessLog("tempStr in reg id is after:"+tempStr);
				/*String[] split = tempStr.split("-");
                 int temp = Integer.parseInt(split[1]);
                 if (temp > max) {
                 max = temp;
                 }*/
                max = Integer.parseInt(tempStr);
            }
            max++;
            logMgr.accessLog("max is :" + max);

            String num8Digit = get8DigitNumber(max);

            registration_id = currentYear + "-" + num8Digit;
            logMgr.accessLog(registration_id);
        } catch (SQLException e) {
            e.printStackTrace();
            //con.close();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return registration_id;
    }
}
