package com.syntaxphoenix.loginplus.encryption;

import java.util.LinkedList;
import java.util.Queue;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.thread.EncryptionCallback;
import com.syntaxphoenix.loginplus.encryption.thread.EncryptionThread;

public class EncryptionManager {
	
	private MainConfig config;
	private int encryptActions = 0;
	private Queue<EncryptionThread> queue;
	
	public EncryptionManager(MainConfig config) {
		this.config = config;
		this.queue = new LinkedList<EncryptionThread>();
	}
	
	public void validatePassword(String password, EncryptionType type, String hash, EncryptionCallback callback) {
		EncryptionThread thread = new EncryptionThread(this, callback, password, type.get(config), hash);
		thread.start();
	}
	
	public void hashPassword(String password, EncryptionType type, EncryptionCallback callback) {
		EncryptionThread thread = new EncryptionThread(this, callback, password, type.get(config));
		thread.start();
	}
	
	public int getEncryptionAction() {
		return this.encryptActions;
	}
	
	public MainConfig getConfig() {
		return this.config;
	}
	
	public boolean canRun(EncryptionThread thread) {
		if (this.queue.isEmpty() && encryptActions < config.getEncryptionTasks()) {
			this.encryptActions++;
			return true;
		}
		this.queue.add(thread);
		return false;
	}
	
	public void endRun() {
		this.encryptActions--;
		if (!this.queue.isEmpty()) {
			EncryptionThread thread = this.queue.remove();
			thread.notify();
		}
	}
}
