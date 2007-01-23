package edu.harvard.fas.rbrady.tpteam.tpmanager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtil {

	public static final String SUCCESS = "true";

	/** The Driver class name for the JDBC Database */
	private static String mDriver = null;

	/** The JDBC URL to the TPTeam database */
	private static String mJDBCURL = null;

	/** The TPTeam database user with admin privileges */
	private static String mDBUser = null;

	/** The TPTeam database user's password */
	private static String mDBPassword = null;

	public static Connection getConn() {
		Connection conn = null;

		try {
			Class.forName(mDriver).newInstance();
			conn = DriverManager.getConnection(mJDBCURL, mDBUser, mDBPassword);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot connect to database server");
		}
		return conn;
	}

	public static void init(String driver, String jdbcURL, String dbUser,
			String dbPassword) {
		mDriver = driver;
		mJDBCURL = jdbcURL;
		mDBUser = dbUser;
		mDBPassword = dbPassword;
	}

	public static void main(String[] args) {
		try {
			DatabaseUtil.init("oracle.jdbc.driver.OracleDriver",
					"jdbc:oracle:thin:@192.168.0.12:1521:xe", "system",
					"tpteam_2");
			Connection conn = DatabaseUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT table_name from user_tables");
			while (rs.next()) {
				System.out.println("table_name: " + rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
