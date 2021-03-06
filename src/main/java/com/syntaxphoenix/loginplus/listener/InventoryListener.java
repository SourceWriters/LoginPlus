package com.syntaxphoenix.loginplus.listener;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.utils.ItemUtils;
import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.captcha.CaptchaInventoryHolder;
import com.syntaxphoenix.loginplus.utils.login.Status;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public InventoryListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Status status = pluginUtils.getUserHandler().getStatus(player);
		if (status != Status.LOGIN && status != Status.REGISTER && status != Status.CAPTCHA && status != Status.LOGIN) {
			return;
		}
		event.setCancelled(true);
		
		if (!(event.getInventory().getHolder() instanceof CaptchaInventoryHolder)) {
			return;
		}
		
		CaptchaInventoryHolder holder = (CaptchaInventoryHolder) event.getInventory().getHolder();
		
		if (holder.getPlayer() != player || event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || 
			!event.getCurrentItem().getItemMeta().hasDisplayName()) {
			return;
		}
		
		String name = event.getCurrentItem().getItemMeta().getDisplayName();
		
		if (name.equalsIgnoreCase(MessagesConfig.captcha_dont_click)) {
			holder.getInventory().clear();
			player.kickPlayer(MessagesConfig.prefix + MessagesConfig.captcha_failed);
		} else if (name.equalsIgnoreCase(MessagesConfig.captcha_change)) {
			int slot = event.getSlot();
			event.getInventory().setItem(slot, ItemUtils.DyeCreator(MessagesConfig.captcha_changed, null, null, 1, DyeColor.LIME));
			holder.removeCaptchaItem();
			if (holder.getCaptchaItems() <= 0) {
				pluginUtils.getUserHandler().setStatus(player, Status.LOGGEDIN);
				player.closeInventory();
				holder.getInventory().clear();
				this.pluginUtils.getLoginManager().loginUser(player, false);
			}
		}
	}
	
	@EventHandler
	public void on(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (pluginUtils.getUserHandler().getStatus(player) == Status.CAPTCHA) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.getInstance(), new Runnable() {
				@Override
				public void run() {
					player.openInventory(new CaptchaInventoryHolder(player).getInventory());
				}	
			}, 1);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(PlayerPickupItemEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus((Player) event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(PlayerDropItemEvent event) {
		Status status = pluginUtils.getUserHandler().getStatus((Player) event.getPlayer());
		if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
			event.setCancelled(true);
		}
	}
}
