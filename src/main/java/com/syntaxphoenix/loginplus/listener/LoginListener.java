package com.syntaxphoenix.loginplus.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.mysql.AccountLoadingThread;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

@SuppressWarnings("deprecation")
public class LoginListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public LoginListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler
	public void on(PlayerPreLoginEvent event) {
		String player = event.getName();
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.getName().toLowerCase().equalsIgnoreCase(player.toLowerCase())) {
				event.disallow(null, MessagesConfig.prefix + MessagesConfig.already_logged_in);
				break;
			}
		}
	}
	
	@EventHandler
	public void on(PlayerLoginEvent event) {
		String ip = event.getAddress().toString().substring(1 , event.getAddress().toString().length()).split(":")[0];
		if (pluginUtils.getTimer().isBannedIp(ip)) {
			event.disallow(
				null,
				MessagesConfig.prefix + MessagesConfig.banned.replace("%BanTime%", this.pluginUtils.getTimer().getRemainingbanTime(ip) + "")
			);
		}
		String username = event.getPlayer().getName();
		
		new AccountLoadingThread(pluginUtils, username).start();
	}
}
