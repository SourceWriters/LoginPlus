package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class PasswordConfig {
	
	public static File f = new File("plugins/LoginPlus", "passwords.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public static List<String> users = new ArrayList<String>();
	
	public static void setPassword(String uuid, String password_hash, String hashtype) {
		cfg.set(uuid + ".password", password_hash);
		cfg.set(uuid + ".encryption.type", hashtype);
		if(!users.contains(uuid)) {
			users.add(uuid);
			cfg.set("users", users);
		}
		save();
	}
	
	public static void setPremium(String uuid, boolean premium) {
		cfg.set(uuid + ".premium", premium);
	}
	
	public static boolean getPremium(String uuid) {
		if (cfg.contains(uuid + ".premium")) {
			return cfg.getBoolean(uuid + ".premium");
		}
		return false;
	}
	
	public static String getHashedPassword(String uuid) {
		if (!users.contains(uuid)) {
			users.add(uuid);
			cfg.set("users", users);
		}
		return cfg.getString(uuid + ".password");
	}
	
	public static EncryptionType getHashtype(String uuid) {
		if (cfg.contains(uuid + ".encryption.type")) {
			return EncryptionType.valueOf(cfg.getString(uuid + ".encryption.type"));
		} else {
			return MainConfig.type;
		}
	}
	
	public static boolean isInDatabase(String uuid) {
		if (cfg.contains(uuid + ".password")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void load() {
		if(cfg.contains("users")) {
			users = cfg.getStringList("users");
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
