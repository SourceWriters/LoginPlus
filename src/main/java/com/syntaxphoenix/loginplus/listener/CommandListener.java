package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class CommandListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			if(e.getMessage().startsWith("/")) {
				e.setCancelled(true);
			}
		}
	}
}
