package 노래추천;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DB{
	static Connection conn = null;
	static Statement stmt = null;
	static Statement stmt1 = null;
	//static String url = "jdbc:mysql://10.96.121.63:3306/song?serverTimezone=UTC";
	String url = "jdbc:mysql://127.0.0.1:3306/song?serverTimezone=UTC";
	static String id = "jiyeon";
	static String password = "jiyeon2329!";
	void connect(){
		try {
			conn = DriverManager.getConnection(url,id, password);
			stmt = conn.createStatement();
			stmt1 =  conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getId() {
		return id;
	}
	public static void setId(String id) {
		DB.id = id;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		DB.password = password;
	}
	
}