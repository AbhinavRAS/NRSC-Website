package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.PhD_Data;
import ehiring.db.DBConnectionManager;

public class PhdOperation {
	
	LogManager logMgr ;
	
	public PhdOperation(String emailLogId) {
			logMgr = LogManager.getInstance(emailLogId);
		}
	
	public int phdData(PhD_Data phd, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = con
					.prepareStatement("insert into phd_data(registration_id,advt_no,post_no,phd_university,phd_college,"
							+ "phd_qualification,phd_discipline,phd_year_of_passing,phd_division,phd_percentage_cgpa,phd_percentage,phd_cgpa_obt,phd_cgpa_max) Values(?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, phd.getRegistration_id());
			pstmt.setString(2, phd.getAdvt_no());
			pstmt.setString(3, phd.getPost_no());
			pstmt.setString(4, phd.getPhd_university());
			pstmt.setString(5, phd.getPhd_college());
			pstmt.setString(6, phd.getPhd_qualification());
			pstmt.setString(6, phd.getPhd_discipline());
			pstmt.setInt(7, phd.getPhd_year_of_passing());
			pstmt.setString(8, phd.getPhd_division());
			pstmt.setString(9, phd.getPhd_percentage_cgpa());
			pstmt.setFloat(10, phd.getPhd_percentage());
			pstmt.setFloat(11, phd.getPhd_cgpa_obt());
			pstmt.setString(12, phd.getPhd_cgpa_max());
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}