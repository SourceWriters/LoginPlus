package com.syntaxphoenix.loginplus;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.syntaxphoenix.loginplus.config.MYSQLConfig;
import com.syntaxphoenix.loginplus.utils.MYSQL;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class LoginPlus extends JavaPlugin {
	
	public static LoginPlus m;
	
	public void onEnable() {
		m = this;
		PluginUtils.setUp();
	}
	
	public void onDisable() {
		for(Player all : PluginUtils.login) {
			all.kickPlayer("");
		}
		for(Player all : PluginUtils.register) {
			all.kickPlayer("");
		}
		for(Player all : PluginUtils.captcha) {
			all.kickPlayer("");
		}
		if(MYSQLConfig.enabled) {
			MYSQL.disconnect();
		}
	}
}
