package ehiring.operation;


import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DesirableQualificationOperation {
	
	public  DBConnectionManager conMgr = null;
	public  String lPoolName="recruit";
	LogManager logMgr;
	
	public  DesirableQualificationOperation(String emailLogId){
		conMgr = DBConnectionManager.getInstance();
		logMgr = LogManager.getInstance(emailLogId);
		}
		
	
	public ArrayList<String> loadDesirableQualOptions() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<String> arr = new ArrayList<String>();
		
		
		try {
			logMgr.accessLog("Inside GET Desirable Qualification OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement("select desirable_qualification_value from desirable_qualification order by desirable_qualification_value",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				arr.add(rs.getString("desirable_qualification_value"));
			}
			if (arr.isEmpty()) {
				logMgr.accessLog("No Data Found");
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