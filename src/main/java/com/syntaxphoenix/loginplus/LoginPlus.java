package com.syntaxphoenix.loginplus;

import org.bukkit.plugin.java.JavaPlugin;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class LoginPlus extends JavaPlugin {
	
	private static LoginPlus instance;
	
	private PluginUtils pluginUtils;
	
	public void onEnable() {
		instance = this;
		this.pluginUtils = new PluginUtils();
	}
	
	public void onDisable() {
		this.pluginUtils.disable();
	}
	
	public PluginUtils getPluginUtils() {
		return this.pluginUtils;
	}
	
	public static LoginPlus getInstance() {
		return instance;
	}
}
