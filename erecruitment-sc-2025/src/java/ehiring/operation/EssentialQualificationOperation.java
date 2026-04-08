package ehiring.operation;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;

public class EssentialQualificationOperation {
	
	public DBConnectionManager conMgr = null;
	public  String lPoolName="recruit";
	LogManager logMgr;
	
	public  EssentialQualificationOperation(String emailLogId){
		conMgr = DBConnectionManager.getInstance();
		logMgr = LogManager.getInstance(emailLogId);
		}
	
	
	public ArrayList<String> loadEssentialQualOptions() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<String> arr = new ArrayList<String>();
		
		try {
			System.out.println(CurrentDateTime.dateTime()+":"+"Inside GET Essential Qualification OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement("select qualification_value from essential_qualification order by qualification_value",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				arr.add(rs.getString("qualification_value"));
			}
			if (arr.isEmpty()) {
				System.out.println(CurrentDateTime.dateTime()+":"+"No Data Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.close();
				pstmt.close();
				conMgr.freeConnection(lPoolName,con);
			} catch (SQLException e) {
			}
		}
		return arr;
	}
}