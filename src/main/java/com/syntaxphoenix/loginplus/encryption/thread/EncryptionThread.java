package com.syntaxphoenix.loginplus.encryption.thread;

import com.syntaxphoenix.loginplus.encryption.EncryptionManager;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class EncryptionThread extends Thread {
	
	private EncryptionThreadMethod method;
	private EncryptionManager manager;
	private EncryptionCallback callback;
	
	private String password;
	private EncryptionType type;
	private String hash;
	
	public EncryptionThread(EncryptionManager manager, EncryptionCallback callback, String password, EncryptionType type) {
		this.method = EncryptionThreadMethod.HASH;
		this.manager = manager;
		this.callback = callback;
		
		this.password = password;
		this.type = type;
	}
	
	public EncryptionThread(EncryptionManager manager, EncryptionCallback callback, String password, EncryptionType type, String hash) {
		this.method = EncryptionThreadMethod.VALIDATE;
		this.manager = manager;
		this.callback = callback;
		
		this.password = password;
		this.type = type;
		this.hash = hash;
	}
	
	public void run() {
		this.checkQueue();
		try {
			long freeMemory = Runtime.getRuntime().freeMemory();
			while (freeMemory < ((manager.getConfig().getArgon2Memory() + 10) * 1024 * 1024)) {
				Thread.sleep(10);
			}
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			exception.printStackTrace();
		}
		if (method == EncryptionThreadMethod.HASH) {
			this.hashPassword();
		} else {
			this.validatePassword();
		}
		this.manager.endRun();
	}
	
	public synchronized void checkQueue() {
		if (!manager.canRun(this)) {
			try {
				wait();
			} catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
				exception.printStackTrace();
			}
		}
	}
	
	public void hashPassword() {
		String hash = this.manager.getEncryptionUtils().hashPassword(this.password, this.type);
		this.callback.encryptCallback(hash);
	}
	
	public void validatePassword() {
		boolean valid = this.manager.getEncryptionUtils().verifyPassword(this.password, this.type, this.hash);
		this.callback.validateCallback(valid);
	}

}
