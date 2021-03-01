package com.syntaxphoenix.loginplus.utils.session;

public class Session {
	
	private long time;
	private String username;
	
	public Session(String username, long time) {
		this.username = username;
		this.time = time;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public String getUsername() {
		return this.username;
	}

}
