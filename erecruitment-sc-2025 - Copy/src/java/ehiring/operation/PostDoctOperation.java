package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.PostDoctoral_Data;

public class PostDoctOperation {
	
LogManager logMgr ;
	
	public PostDoctOperation(String emailLogId) {
			logMgr = LogManager.getInstance(emailLogId);
		}
	
	public int pdData(PostDoctoral_Data pdd,Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = con.prepareStatement("insert into pd_data(registration_id,advt_no,post_no,pd_university,pd_college,"
					+ "pd_qualification,pd_discipline,pd_year_of_passing,pd_division,pd_percentage_cgpa,pd_percentage,pd_cgpa_obt,pd_cgpa_max) Values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, pdd.getRegistration_id());
			pstmt.setString(2, pdd.getAdvt_no());
			pstmt.setString(3, pdd.getPost_no());
			pstmt.setString(4, pdd.getPd_university());
			pstmt.setString(5, pdd.getPd_college());
			pstmt.setString(6, pdd.getPd_qualification());
			pstmt.setString(7, pdd.getPd_discipline());
			pstmt.setInt(8, pdd.getPd_year_of_passing());
			pstmt.setString(9, pdd.getPd_division());
			pstmt.setString(10, pdd.getPd_percentage_cgpa());
			pstmt.setFloat(11, pdd.getPd_percentage());
			pstmt.setFloat(12, pdd.getPd_cgpa_obt());
			pstmt.setString(13, pdd.getPd_cgpa_max());
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}