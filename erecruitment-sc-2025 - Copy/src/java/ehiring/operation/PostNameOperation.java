package ehiring.operation;


import ehiring.db.DBConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostNameOperation {
	
	public DBConnectionManager conMgr = null;
	public String lPoolName="recruit";
	
	public PostNameOperation(){
		conMgr = DBConnectionManager.getInstance();
		}
	
	
	public ArrayList<String> loadPostNameOptions() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<String> arr = new ArrayList<String>();
		
		try {
			System.out.println("Inside GET Post Name OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement("select post_name_value from post_name", ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				arr.add(rs.getString("post_name_value"));
			}
			if (arr.isEmpty()) {
				System.out.println("No Data Found");
			}
		}
		catch (Exception e) {
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