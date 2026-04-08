package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.Undergrad_Data;
import ehiring.db.DBConnectionManager;

public class UgOperation {
	
	LogManager logMgr;

	public UgOperation(String emailLogId) {
			logMgr = LogManager.getInstance(emailLogId);
	}
	public int ugData(Undergrad_Data ugd,Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = con.prepareStatement("insert into ug_data(registration_id,advt_no,post_no,ug_sl_no,ug_university,ug_college,"
					+ "ug_qualification,ug_discipline,ug_year_of_passing,ug_division,ug_percentage_cgpa,ug_percentage,ug_cgpa_obt,ug_cgpa_max) Values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, ugd.getRegistration_id());
			pstmt.setString(2, ugd.getAdvt_no());
			pstmt.setString(3, ugd.getPost_no());
			pstmt.setInt(4, ugd.getUgSno());
			pstmt.setString(5, ugd.getUg_university());
			pstmt.setString(6, ugd.getUg_college());
			pstmt.setString(7, ugd.getUg_qualification());
			pstmt.setString(8, ugd.getUg_discipline());
			pstmt.setInt(9, ugd.getUg_year_of_passing());
			pstmt.setString(10, ugd.getUg_division());
			pstmt.setString(12, ugd.getUg_percentage_cgpa());
			pstmt.setFloat(12, ugd.getUg_percentage());
			pstmt.setFloat(13, ugd.getUg_cgpa_obt());
			pstmt.setString(14, ugd.getUg_cgpa_max());
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}