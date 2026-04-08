package ehiring.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class JDBCExamplePro {

	public static void main(String[] args) {

		// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
		// auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver
		// class via Class.forName

		// register JDBC driver, optional, since java 1.6
		/*
		 * try { Class.forName("org.postgresql.Driver"); } catch (ClassNotFoundException
		 * e) { e.printStackTrace(); }
		 */

		// auto close connection
		// try (Connection conn =
		// DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/erecruit",
		// "nrsc",
		// "tiger")) {
		try {
			Connection conn = null;
			
			DBConnectionManager conMgr = null;
			conMgr = DBConnectionManager.getInstance();
			conn = conMgr.getConnection("recruit");
			System.out.println("CON IS ..."+conn);
			String lPoolName = "recruit";
			if (conn != null) {
				System.out.println("Connected to the database!");
			} else {
				System.out.println("Failed to make connection!");
			}

		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
}
