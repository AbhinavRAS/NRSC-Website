package ehiring.operation;

	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;

	import ehiring.dao.LogManager;
	import ehiring.dao.NET_Data;
	import ehiring.db.DBConnectionManager;

	public class NETOperation {
		
		LogManager logMgr;

		public NETOperation(String emailLogId) {
				logMgr = LogManager.getInstance(emailLogId);
		}
		
		public int insNETData(NET_Data nd,Connection con) throws SQLException {
			PreparedStatement pstmt = null;
			int i = 0;
			try {
				pstmt = con.prepareStatement(
						"insert into NATIONALEXAMTEST(registration_id,advt_no,post_no,net_name,net_doc) Values(?,?,?,?,?)");

				pstmt.setString(1, nd.getRegistration_id());
				pstmt.setString(2, nd.getAdvt_no());
				pstmt.setString(3, nd.getPost_no());
				pstmt.setString(4, nd.getNet_name());
				pstmt.setString(5, nd.getNet_doc());
				
				logMgr.accessLog(" NET DETAILS ARE:"+pstmt.toString());

				i = pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return i;
		}
	}

