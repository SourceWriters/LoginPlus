package com.syntaxphoenix.loginplus.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class InventoryClearListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public InventoryClearListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void on(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Status status = pluginUtils.getUserHandler().getStatus(player);
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			Inventory inventoryCopy = Bukkit.createInventory(null, 54);
			inventoryCopy.setContents(player.getInventory().getContents());
			PluginUtils.inventories.put(player, inventoryCopy);
			player.getInventory().clear();
		}
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus(event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			setInventory(event.getPlayer());
		}
	}
	
	public static void setInventory(Player player) {
		if (PluginUtils.inventories.containsKey(player)) {
			Inventory copy = PluginUtils.inventories.get(player);
			for (int position = 0; position < player.getInventory().getSize(); position++) {
				player.getInventory().setItem(position, copy.getItem(position));
			}
			PluginUtils.inventories.remove(player);
		}
	}

}
