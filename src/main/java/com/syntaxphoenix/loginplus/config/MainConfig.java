package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class MainConfig extends Config {
	
	private boolean timerEnabled;
	private int timerTime;
	
	private int maxLoginAttempts;
	private boolean loginFailedBan;
	private boolean loginFailedKick;
	private int loginFailedBanTime;
	private boolean loginFailedCommandsEnabled;
	private List<String> loginFailedCommands = new ArrayList<String>();
	
	private boolean reconnectTimeEnabled;
	private int reconnectTime;
	
	private int titleTime;
	
	private boolean captchaEnabled;
	private boolean captchaOnLogin;
	private boolean captchaOnRegister;
	
	private EncryptionType encryptionType = EncryptionType.ARGON_2;
	private int encryptTasks;
	
	private int argon2Cores;
	private int argon2Memory;
	private int argon2Parallelism;
	
	private int bcryptRounds;
	
	public MainConfig() {
		super(new File("plugins/LoginPlus", "config.yml"));
		
		loginFailedCommands.add("kick %Player%");
				
		timerEnabled = set("timer.enabled", true);
		timerTime = set("timer.time", 60);
		
		maxLoginAttempts = set("login.max_attempts", 3);
		loginFailedBan = set("login.ban.enabled", true);
		loginFailedKick = set("login.kick.enabled", true);
		loginFailedBanTime = set("login.ban.time", 120);
		loginFailedCommandsEnabled = set("login.commands.enabled", false);
		loginFailedCommands = set("login.commands.commands", loginFailedCommands);
		
		reconnectTimeEnabled = set("reconnect_time.enabled", true);
		reconnectTime = set("reconnect_time.time", 30);
		
		captchaEnabled = set("captcha.enabled", false);
		captchaOnLogin = set("captcha.login", true);
		captchaOnRegister = set("captcha.register", true);
		
		titleTime = set("title.time", 5);
		
		encryptionType = EncryptionType.valueOf(set("encryption.type", encryptionType.toString()));
		encryptTasks = set("encryption.tasks", 5);
		
		argon2Cores = set("encryption.argon2.cores", Runtime.getRuntime().availableProcessors());
		argon2Memory = set("encryption.argon2.memory", this.getArgon2DefaultMemory());
		argon2Parallelism = set("encryption.argon2.parallelism", Runtime.getRuntime().availableProcessors() * 2);
		
		bcryptRounds = set("encryption.bcrypt.rounds", 14);
	}
	
	public boolean isTimerEnabled() {
		return this.timerEnabled;
	}
	
	public int getTimerTime() {
		return this.timerTime;
	}
	
	public int getTitleTime() {
		return this.titleTime;
	}
	
	public EncryptionType getEncryptionType() {
		return this.encryptionType;
	}
	
	public int getEncryptionTasks() {
		return this.encryptTasks;
	}
	
	public int getArgon2Cores() {
		return this.argon2Cores;
	}
	
	public int getArgon2Memory() {
		return this.argon2Memory;
	}
	
	public int getArgon2Parallelism() {
		return this.argon2Parallelism;
	}
	
	public int getBcryptRounds() {
		return this.bcryptRounds;
	}
	
	public int getLoginFailedBanTime() {
		return this.loginFailedBanTime;
	}
	
	public boolean isLoginFailedBan() {
		return this.loginFailedBan;
	}
	
	public boolean isLoginFailedKick() {
		return this.loginFailedKick;
	}
	
	public boolean isReconnectTimeEnabled() {
		return this.reconnectTimeEnabled;
	}
	
	public int getReconnectTime() {
		return this.reconnectTime;
	}
	
	public List<String> getLoginFailedCommands() {
		return this.loginFailedCommands;
	}
	
	public boolean isLoginFailedCommandsEnabled() {
		return this.loginFailedCommandsEnabled;
	}
	
	public boolean isCaptchaEnabled() {
		return this.captchaEnabled;
	}
	
	public boolean isCaptchaOnLogin() {
		return this.captchaOnLogin;
	}
	
	public boolean isCaptchaOnRegister() {
		return this.captchaOnRegister;
	}
	
	public int getMaxLoginAttempts() {
		return this.maxLoginAttempts;
	}
	
	private int getArgon2DefaultMemory() {
		long totalMemory = Runtime.getRuntime().totalMemory();
		long argon2Usable = totalMemory / 40;
		double megaBytes = argon2Usable / 1024.0D / 1024.0D;
		int[] values = {64, 32, 16, 8, 4, 2};
		
		for (int value : values) {
			if (megaBytes > value) {
				return value;
			}
		}
		return 1;
	}
}
