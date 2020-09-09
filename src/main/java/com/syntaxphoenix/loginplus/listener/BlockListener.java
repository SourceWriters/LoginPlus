package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class BlockListener implements Listener {
	
	@EventHandler
	public void on(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			e.setCancelled(true);
		}
	}
}
