package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.captcha.CaptchaInventoryHolder;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class InteractListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public InteractListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR ||
			event.getAction() == Action.RIGHT_CLICK_BLOCK ||
			event.getAction() == Action.LEFT_CLICK_AIR ||
			event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Status status = pluginUtils.getUserHandler().getStatus(event.getPlayer());
			if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(InventoryOpenEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus((Player) event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			if (event.getInventory().getHolder() instanceof CaptchaInventoryHolder) {
				return;
			}
			event.setCancelled(true);
		}
	}
}
