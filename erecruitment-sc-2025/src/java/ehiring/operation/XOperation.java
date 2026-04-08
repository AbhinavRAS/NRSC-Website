package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.X_Data;
import ehiring.db.DBConnectionManager;

public class XOperation {

    LogManager logMgr;

    public XOperation(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public int xData(X_Data xd, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            // ChangeId: 2023121901 x_cgpa_to_perc added
            pstmt = con.prepareStatement(
                    "insert into x_data(registration_id,advt_no,post_no,x_edu_board,x_school,x_year_of_passing,x_division,"
                    + "x_percentage_cgpa,x_percentage,x_cgpa_obt,x_cgpa_max,marksheet,email,x_cgpa_to_perc) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, xd.getRegistration_id());
            pstmt.setString(2, xd.getAdvt_no());
            pstmt.setString(3, xd.getPost_no());
            pstmt.setString(4, xd.getX_edu_board());
            pstmt.setString(5, xd.getX_school());
            pstmt.setInt(6, xd.getX_year_of_passing());
            pstmt.setString(7, xd.getX_division());
            pstmt.setString(8, xd.getX_percentage_cgpa());
            pstmt.setFloat(9, xd.getX_percentage());
            pstmt.setFloat(10, xd.getX_cgpa_obt());
            pstmt.setString(11, xd.getX_cgpa_max());
            pstmt.setString(12, xd.getMarksheet());
            pstmt.setString(13, xd.getEmail());
            pstmt.setFloat(14, xd.getX_cgpa_to_perc()); // ChangeId: 2023121901
            logMgr.accessLog(pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
