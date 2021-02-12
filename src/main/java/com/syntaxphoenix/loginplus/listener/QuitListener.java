package com.syntaxphoenix.loginplus.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class QuitListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public QuitListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	public void on(PlayerQuitEvent event) {
		this.pluginUtils.getAccountManager().clearLocalAccount(event.getPlayer().getName());
	}

}
