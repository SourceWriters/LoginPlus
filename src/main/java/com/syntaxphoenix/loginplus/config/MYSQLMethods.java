package com.syntaxphoenix.loginplus.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.syntaxphoenix.loginplus.utils.MYSQL;

public class MYSQLMethods {
	
	public static void changeAccount(String uuid, String password, String hashtype, boolean premium) throws SQLException {
		uuid = uuid.replace("-", "");
		PreparedStatement st = null;
		
		if(isInMYSQLTable(uuid)) {
			st = MYSQL.con.prepareStatement("UPDATE `UserData` SET `Password` = ?, `HashType` = ?, `Premium` = ? WHERE `UUID` = ?");
			st.setString(1, password);
			st.setString(2, hashtype);
			st.setBoolean(3, premium);
			st.setString(4, uuid);
		} else {
			st = MYSQL.con.prepareStatement("INSERT INTO `UserData`(`UUID`,`Password`,`HashType`,`Premium`) VALUES (?,?,?,?)");
			st.setString(1, uuid);
			st.setString(2, password);
			st.setString(3, hashtype);
			st.setBoolean(4, premium);
		}
		st.execute();
	}
	
	public static String getPassword(String uuid) throws SQLException {
		uuid = uuid.replace("-", "");
		String result = "";
		
		PreparedStatement st = MYSQL.con.prepareStatement("SELECT * FROM `UserData` WHERE `UUID` = ?");
		st.setString(1, uuid);
		
		ResultSet rs = null;
		
		rs = st.executeQuery();
		
		while(rs.next()) {
			result = rs.getString("Password");
		}
		
		return result;
	}
	
	public static String getHashType(String uuid) throws SQLException {
		uuid = uuid.replace("-", "");
		String result = "";
		
		PreparedStatement st = MYSQL.con.prepareStatement("SELECT * FROM `UserData` WHERE `UUID` = ?");
		st.setString(1, uuid);
		
		ResultSet rs = null;
		
		rs = st.executeQuery();
		
		while(rs.next()) {
			result = rs.getString("HashType");
		}
		
		return result;
	}
	
	public static boolean isPremium(String uuid) throws SQLException {
		uuid = uuid.replace("-", "");
		boolean result = false;
		
		PreparedStatement st = MYSQL.con.prepareStatement("SELECT * FROM `UserData` WHERE `UUID` = ?");
		st.setString(1, uuid);
		
		ResultSet rs = null;
		
		rs = st.executeQuery();
		
		while(rs.next()) {
			result = rs.getBoolean("Premium");
		}
		
		return result;
	}
	
	public static boolean isInMYSQLTable(String uuid) {
		uuid = uuid.replace("-", "");
		try {
			Statement st = MYSQL.con.createStatement();			
			ResultSet rs = null;			
			rs = st.executeQuery("SELECT * FROM `UserData` WHERE `UUID`='"+ uuid +"'");
			
			while(rs.next()) {
			
			if(rs != null) {
				return true;
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;		
	}

}
