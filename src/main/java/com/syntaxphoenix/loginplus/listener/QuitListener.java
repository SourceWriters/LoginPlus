package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class QuitListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public QuitListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		this.pluginUtils.getAccountManager().clearLocalAccount(player.getName());
		
		if (pluginUtils.getConfig().isSessionsEnabled() && !this.pluginUtils.getUserHandler().hasStatus(player)) {
			String ip = player.getAddress().toString().substring(1 , player.getAddress().toString().length()).split(":")[0];
			this.pluginUtils.getSessionManager().addSession(ip, player.getName());
		}
	}

}
