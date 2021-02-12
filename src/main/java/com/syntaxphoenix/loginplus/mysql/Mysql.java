package com.syntaxphoenix.loginplus.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql {
	
	private Connection connection;
	
	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	
	public Mysql(String host, int port, String database, String user, String password) {	
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
		connect();
	}
	
	public void connect() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("MYSQL diconnected!");
		}
	}
	
	public void disconnect() {
		try {
			this.connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public boolean hasConnection() {		
		if (this.connection != null) {
			return true;
		}		
		return false;		
	}
	
	public Connection getConnection() {
		if (!this.hasConnection()) {
			this.connect();
		}
		return this.connection;		
	}
	
}
