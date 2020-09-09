package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class InteractListener implements Listener {
	
	@EventHandler
	public void on(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
}
