package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class JoinListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public JoinListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler
	public void on(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		this.pluginUtils.getUserHandler().setStatus(player, Status.LOGGEDIN);
		this.pluginUtils.getLoginManager().loginUser(player, true);
	}
}
