package com.syntaxphoenix.loginplus.encryption.thread;

import org.bukkit.Bukkit;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.encryption.EncryptionManager;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.encryption.EncryptionUtils;

public class EncryptionThread extends Thread {
	
	private EncryptionThreadMethod method;
	private EncryptionManager manager;
	private EncryptionCallback callback;
	private EncryptionUtils encryptionUtils;
	
	private String password;
	private EncryptionType type;
	private String hash;
	
	public EncryptionThread(EncryptionManager manager, EncryptionUtils encryptionUtils, EncryptionCallback callback, String password, EncryptionType type) {
		this.method = EncryptionThreadMethod.HASH;
		this.manager = manager;
		this.callback = callback;
		this.encryptionUtils = encryptionUtils;
		
		this.password = password;
		this.type = type;
	}
	
	public EncryptionThread(EncryptionManager manager, EncryptionUtils encryptionUtils, EncryptionCallback callback, String password, EncryptionType type, String hash) {
		this.method = EncryptionThreadMethod.VALIDATE;
		this.manager = manager;
		this.callback = callback;
		this.encryptionUtils = encryptionUtils;
		
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
	
	private void hashPassword() {
		String hash = this.encryptionUtils.hashPassword(this.password, this.type);
		Bukkit.getScheduler().runTask(LoginPlus.getInstance(), new Runnable() {
			@Override
			public void run() {
				callback.encryptCallback(hash);
				manager.endRun();
			}		
		});
	}
	
	private void validatePassword() {
		boolean valid = this.encryptionUtils.verifyPassword(this.password, this.type, this.hash);
		Bukkit.getScheduler().runTask(LoginPlus.getInstance(), new Runnable() {
			@Override
			public void run() {
				callback.validateCallback(valid);
				manager.endRun();
			}		
		});
	}

}
