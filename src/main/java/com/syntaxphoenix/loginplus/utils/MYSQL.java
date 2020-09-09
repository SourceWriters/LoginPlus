package com.syntaxphoenix.loginplus.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MYSQL {
	
	public static Connection con;	
	
	public static Connection connect(String host, int port, String database, String user, String password) {	
		if(!hasConnection()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
			} catch (SQLException e) {
				System.out.println("§cMySQL konnte sich nicht verbinden:" + e.getMessage());
				e.printStackTrace();
			}
		}
		return con;
	}
	
	public static void disconnect(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasConnection() {		
		if(con != null) {
			return true;
		}
		
		return false;		
	}
	
	public static Connection getConnection() {	
		return con;	
	}
}
