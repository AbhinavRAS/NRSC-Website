package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;

public class EducationalBoardOperation {

    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    LogManager logMgr;

    public EducationalBoardOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
    }

    public ArrayList<String> loadBoardNameOptions() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<String>();

        try {
            logMgr.accessLog("Inside loadBoardNameOptions");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select edu_board_name from edu_board order by edu_board_name", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                arr.add(rs.getString("edu_board_name"));
            }
            if (arr.isEmpty()) {
                logMgr.accessLog("No Data Found");
            }
        } catch (Exception e) {
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
}
