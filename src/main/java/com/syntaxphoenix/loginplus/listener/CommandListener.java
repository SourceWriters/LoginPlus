package com.syntaxphoenix.loginplus.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class CommandListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public CommandListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerCommandPreprocessEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus(event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			if (event.getMessage().startsWith("/")) {
				event.setCancelled(true);
			}
		}
	}
}
