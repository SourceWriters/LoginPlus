package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MYSQLConfig {
	
	public static File f = new File("plugins/LoginPlus", "mysql.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public static boolean enabled = false;
	public static String address = "localhost";
	public static int port = 3306;
	public static String database = "LoginPlus";
	public static String username = "username";
	public static String password = "password";
	
	public static void load() {
		enabled = setObject("mysql.enabled", enabled);
		address = setObject("mysql.address", address);
		port = setObject("mysql.port", port);
		database = setObject("mysql.database", database);
		username = setObject("mysql.username", username);
		password = setObject("mysql.password", password);
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
	
	public static String setObject(String path, String obj) {
		if(cfg.contains(path)) {
			return cfg.getString(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
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
	
	public static void save() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
