package com.syntaxphoenix.loginplus.utils.session;

import java.util.HashMap;

import com.syntaxphoenix.loginplus.config.MainConfig;

public class SessionManager {
	
	private MainConfig config;
	private HashMap<String, Session> sessions;
	
	public SessionManager(MainConfig config) {
		this.config = config;
		this.sessions = new HashMap<String, Session>();
	}
	
	public void addSession(String ip, String username) {
		this.sessions.put(ip, new Session(username, System.currentTimeMillis()));
	}
	
	public boolean validateSession(String ip, String username) {
		if (!this.sessions.containsKey(ip) ||
			(this.sessions.get(ip).getTime() + (1000 * config.getSessionTime())) < System.currentTimeMillis() ||
			!this.sessions.get(ip).getUsername().equalsIgnoreCase(username)) {
			return false;
		}
		return true;
	}
	
	public void resetSession(String ip) {
		this.sessions.remove(ip);
	}

}
