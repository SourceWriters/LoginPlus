package com.syntaxphoenix.loginplus.config;

import java.io.File;

public class MysqlConfig extends Config {
	
	private boolean enabled;
	private String address;
	private int port;
	private String database;
	private String username;
	private String password;
	
	public MysqlConfig() {
		super(new File("plugins/LoginPlus", "mysql.yml"));
		
		enabled = set("mysql.enabled", false);
		address = set("mysql.address", "localhost");
		port = set("mysql.port", 3306);
		database = set("mysql.database", "loginplus");
		username = set("mysql.username", "username");
		password = set("mysql.password", "password");
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
