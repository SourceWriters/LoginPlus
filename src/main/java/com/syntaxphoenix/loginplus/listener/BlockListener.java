package com.syntaxphoenix.loginplus.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class BlockListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public BlockListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler
	public void on(BlockBreakEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus(event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(BlockPlaceEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus(event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			event.setCancelled(true);
		}
	}
}
