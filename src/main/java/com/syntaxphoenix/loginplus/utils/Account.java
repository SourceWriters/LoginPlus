package com.syntaxphoenix.loginplus.utils;

import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class Account {
	
	private String uuid;
	private String password;
	private EncryptionType hashtype;
	private boolean premium;
	
	public Account(String uuid, String password, EncryptionType hashtype, boolean premium) {
		this.uuid = uuid;
		this.password = password;
		this.hashtype = hashtype;
		this.premium = premium;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setHashType(EncryptionType hashtype) {
		this.hashtype = hashtype;
	}
	
	public EncryptionType getHashType() {
		return this.hashtype;
	}
	
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
	public boolean isPremium() {
		return this.premium;
	}
}
