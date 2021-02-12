package com.syntaxphoenix.loginplus.accounts;

import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class Account {
	
	private String username;
	private String hash;
	private EncryptionType type;
	private boolean premium;
	
	public Account(String username, String hash, EncryptionType type, boolean premium) {
		this.username = username;
		this.hash = hash;
		this.type = type;
		this.premium = premium;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getHash() {
		return this.hash;
	}
	
	public void setType(EncryptionType type) {
		this.type = type;
	}
	
	public EncryptionType getType() {
		return this.type;
	}
	
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
	public boolean isPremium() {
		return this.premium;
	}
}
