package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.Postgrad_Data;

public class PgOperation {

	LogManager logMgr;

	public PgOperation(String emailLogId) {
		logMgr = LogManager.getInstance(emailLogId);
	}

	public int pgData(Postgrad_Data pgd, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = con.prepareStatement(
					"insert into pg_data(registration_id,advt_no,post_no,pg_sl_no,pg_university,pg_college,"
							+ "pg_qualification,pg_discipline,pg_year_of_passing,pg_division,pg_percentage_cgpa,pg_percentage,pg_cgpa_obt,pg_cgpa_max) Values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, pgd.getRegistration_id());
			pstmt.setString(2, pgd.getAdvt_no());
			pstmt.setString(3, pgd.getPost_no());
			pstmt.setInt(4, pgd.getPgSno());
			pstmt.setString(5, pgd.getPg_university());
			pstmt.setString(6, pgd.getPg_college());
			pstmt.setString(7, pgd.getPg_qualification());
			pstmt.setString(8, pgd.getPg_discipline());
			pstmt.setInt(9, pgd.getPg_year_of_passing());
			pstmt.setString(10, pgd.getPg_division());
			pstmt.setString(11, pgd.getPg_percentage_cgpa());
			pstmt.setFloat(12, pgd.getPg_percentage());
			pstmt.setFloat(13, pgd.getPg_cgpa_obt());
			pstmt.setString(14, pgd.getPg_cgpa_max());
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}