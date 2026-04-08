package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.XII_Data;
import ehiring.db.DBConnectionManager;

public class XiiOperation {
	
	LogManager logMgr;

	public XiiOperation(String emailLogId) {
			logMgr = LogManager.getInstance(emailLogId);
	}
	
	public int xiiData(XII_Data xiid,Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
                        // ChangeId: 2023122001 xii_cgpa_to_perc added
			pstmt = con
					.prepareStatement("insert into xii_data(registration_id,advt_no,post_no,xii_edu_board,xii_school,"
							+ "xii_specialization,xii_year_of_passing,xii_division,xii_percentage_cgpa,xii_percentage,xii_cgpa_obt,xii_cgpa_max,marksheet,email,xii_cgpa_to_perc) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, xiid.getRegistration_id());
			pstmt.setString(2, xiid.getAdvt_no());
			pstmt.setString(3, xiid.getPost_no());
			pstmt.setString(4, xiid.getXii_edu_board());
			pstmt.setString(5, xiid.getXii_school());
			pstmt.setString(6, xiid.getXii_specialization());
			pstmt.setInt(7, xiid.getXii_year_of_passing());
			pstmt.setString(8, xiid.getXii_division());
			pstmt.setString(9, xiid.getXii_percentage_cgpa());
			pstmt.setFloat(10, xiid.getXii_percentage());
			pstmt.setFloat(11, xiid.getXii_cgpa_obt());
			pstmt.setString(12, xiid.getXii_cgpa_max());
			pstmt.setString(13, xiid.getMarksheet());
                        pstmt.setString(14, xiid.getEmail());
                        pstmt.setFloat(15, xiid.getXii_cgpa_to_perc()); // ChangeId: 2023122001
			
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}