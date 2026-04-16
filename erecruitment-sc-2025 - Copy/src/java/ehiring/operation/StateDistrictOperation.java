package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;

public class StateDistrictOperation {

    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    LogManager logMgr;

    public StateDistrictOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
    }

    public ArrayList<String> loadStateNameOptions() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<String>();

        try {
            System.out.println("Inside GET State Name OPTIONS");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select state_name from states  order by state_name asc ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                arr.add(rs.getString("state_name"));
            }
            if (arr.isEmpty()) {
                System.out.println("No Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }

        return arr;
    }

    public ArrayList<String> loadDistrictNameOptions(String state) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<String>();

        try {
            System.out.println("inside load district name options");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select district_name from districts d, states s where s.state_id=d.state_id and s.state_name=? order by district_name asc ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, state);
            rs = pstmt.executeQuery();
            System.out.println(pstmt.toString());
            while (rs.next()) {
                arr.add(rs.getString("district_name"));
            }
            if (arr.isEmpty()) {
                System.out.println("No Data Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return arr;
    }

    public ArrayList loadUniversity() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList formattedList = new ArrayList();

        try {
            System.out.println("inside load University name options");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select university_name from university order by university_name asc",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                formattedList.add(rs.getString("university_name"));
            }
            System.out.println(pstmt.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return formattedList;
    }

    public ArrayList loadNET() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList formattedList = new ArrayList();

        try {
            System.out.println("inside load NET name options");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select NET_name from NET order by net_name asc",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                formattedList.add(rs.getString("NET_name"));
            }
            System.out.println(pstmt.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return formattedList;
    }
}
