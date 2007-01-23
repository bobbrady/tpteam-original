package edu.harvard.fas.rbrady.tpteam.tpmanager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleTest {

	/**
	 * @param args
	 */
	public static void dbConnect() {
		Connection conn = null;

		try {
			String userName = "tpmgr";
			String password = "tpteam";
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			System.out.println("Database connection established");
			
			  Statement s = conn.createStatement();
			   s.executeQuery ("SELECT * FROM tpteam_user");
			   ResultSet rs = s.getResultSet ();
			   int count = 0;
			   while (rs.next ())
			   {
			       int idVal = rs.getInt ("user_id");
			       String fnameVal = rs.getString ("first_name");
			       String lnameVal = rs.getString ("last_name");
			       System.out.println (
			               "id = " + idVal
			               + ", f_name = " + fnameVal
			               + ", l_name = " + lnameVal);
			       ++count;
			   }
			   rs.close ();
			   s.close ();
			   System.out.println (count + " rows were retrieved");
			
			
		} catch (Exception e) {
			System.err.println("Cannot connect to database server");
		} finally {
			if (conn != null) {
				try {
					conn.close();
					System.out.println("Database connection terminated");
				} catch (Exception e) { /* ignore close errors */
				}
			}

		}
	}
}
