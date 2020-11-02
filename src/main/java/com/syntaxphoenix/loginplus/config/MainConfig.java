package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class MainConfig {
	
	public static File f = new File("plugins/LoginPlus", "config.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public static boolean timer_enabled = true;
	public static int timer_time = 60;
	public static EncryptionType type = EncryptionType.ARGON_2;
	public static int login_attempts = 3;
	public static boolean reconnect_time_enabled = true;
	public static int reconnect_time = 30;
	public static boolean login_failed_ban = true;
	public static boolean login_failed_kick = true;
	public static int login_failed_ban_time = 120;
	public static boolean login_failed_commands_enabled = false;
	public static List<String> login_failed_commands = new ArrayList<String>();
	
	public static int titleTime = 5;
	
	public static boolean captcha = false;
	public static boolean captcha_on_login = true;
	public static boolean captcha_on_register = true;
	
	public static int argon2Cores = 4;
	public static int argon2Memory = 1024;
	public static int argon2Parallelism = 8;
	
	public static int bcryptRounds = 14;
	
	
	public static void load() {
		login_failed_commands.add("kick %Player%");
				
		timer_enabled = setObject("timer.enabled", timer_enabled);
		timer_time = setObject("timer.time", timer_time);
		type = EncryptionType.valueOf(setObject("encryption.type", type.toString()));
		login_attempts = setObject("login.max_attempts", login_attempts);
		login_failed_ban = setObject("login.ban.enabled", login_failed_ban);
		login_failed_kick = setObject("login.kick.enabled", login_failed_kick);
		login_failed_ban_time = setObject("login.ban.time", login_failed_ban_time);
		login_failed_commands_enabled = setObject("login.commands.enabled", login_failed_commands_enabled);
		login_failed_commands = setObject("login.commands.commands", login_failed_commands);
		reconnect_time_enabled = setObject("reconnect_time.enabled", reconnect_time_enabled);
		reconnect_time = setObject("reconnect_time.time", reconnect_time);
		captcha = setObject("captcha.enabled", captcha);
		captcha_on_login = setObject("captcha.login", captcha_on_login);
		captcha_on_register = setObject("captcha.register", captcha_on_register);
		
		titleTime = setObject("title.time", titleTime);
		
		argon2Cores = setObject("encryption.argon2.cores", argon2Cores);
		argon2Memory = setObject("encryption.argon2.memory", argon2Memory);
		argon2Parallelism = setObject("encryption.argon2.parallelism", argon2Parallelism);
		bcryptRounds = setObject("encryption.bcrypt.rounds", bcryptRounds);
	}

	public static int setObject(String path, int obj) {
		if(cfg.contains(path)) {
			return cfg.getInt(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
	}
	
	public static String setObject(String path, String obj) {
		if(cfg.contains(path)) {
			return cfg.getString(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
	}
	
	public static boolean setObject(String path, boolean obj) {
		if(cfg.contains(path)) {
			return cfg.getBoolean(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
	}
	
	public static List<String> setObject(String path, List<String> obj) {
		if(cfg.contains(path)) {
			return cfg.getStringList(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
	}
	
	public static void save() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
