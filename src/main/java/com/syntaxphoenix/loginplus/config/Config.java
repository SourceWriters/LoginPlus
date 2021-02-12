package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Config {
	
	protected File file;
	protected FileConfiguration configuration;
	
	public Config(File file) {
		this.file = file;
		this.configuration = YamlConfiguration.loadConfiguration(file);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T set(String path, T value) {
		if (this.configuration.contains(path)) {
			return (T) this.configuration.get(path);
		} else {
			this.configuration.set(path, value);
			save();
			return value;
		}
	}
	
	public void save() {
		try {
			this.configuration.save(this.file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
