package com.syntaxphoenix.loginplus.listener;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.config.PasswordConfig;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;
import com.syntaxphoenix.loginplus.utils.CaptchaUtils;
import com.syntaxphoenix.loginplus.utils.ItemUtils;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void on(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			if(!e.getView().getTitle().equalsIgnoreCase(MessagesConfig.captcha_name)) {
				e.setCancelled(true);
			} else {
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().hasItemMeta()) {
						if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
							String disp = e.getCurrentItem().getItemMeta().getDisplayName();
							if(disp.equalsIgnoreCase(MessagesConfig.captcha_dont_click)) {
								CaptchaUtils.captchaParts.remove(p);
								p.kickPlayer(MessagesConfig.prefix + MessagesConfig.captcha_failed);
							} else if(disp.equalsIgnoreCase(MessagesConfig.captcha_change)) {
								int slot = e.getSlot();
								e.getInventory().setItem(slot, ItemUtils.DyeCreator(MessagesConfig.captcha_changed, null, null, 1, DyeColor.LIME));
								CaptchaUtils.captchaParts.put(p, CaptchaUtils.captchaParts.get(p)-1);
								if(CaptchaUtils.captchaParts.get(p) <= 0) {
									PluginUtils.captcha.remove(p);
									p.closeInventory();
									if(PasswordConfig.isInDatabase(p.getUniqueId().toString())) {
										if(PasswordConfig.getPremium(p.getUniqueId().toString())) {
											if(!PremiumCheck.isPremium(p)) {
												p.kickPlayer(MessagesConfig.prefix + MessagesConfig.no_premium);
											}
											PluginUtils.login.remove(p);
											PluginUtils.timer.remove(p);
										} else {
											try {
												GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_login_title, MessagesConfig.title_login_subtitle);
											} catch (InstantiationException | IllegalAccessException
													| IllegalArgumentException | InvocationTargetException
													| NoSuchMethodException | SecurityException
													| NoSuchFieldException e1) {
												e1.printStackTrace();
											}
											PluginUtils.login.add(p);
											if(MainConfig.timer_enabled) {
												PluginUtils.timer.put(p, 0);
											}
											PluginUtils.attempts.put(p, MainConfig.login_attempts);
										}
									} else {
										try {
											GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_register_title, MessagesConfig.title_register_subtitle);
										} catch (InstantiationException | IllegalAccessException
												| IllegalArgumentException | InvocationTargetException
												| NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
											e1.printStackTrace();
										}
										PluginUtils.register.add(p);
									}
								}
							}
						}
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void on(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(PluginUtils.captcha.contains(p)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.m, new Runnable() {
				@Override
				public void run() {
					p.openInventory(CaptchaUtils.createCaptchaInventory(p));
				}	
			}, 1);
		}
	}
	
	@EventHandler
	public void on(PlayerPickupItemEvent e) {
		Player p = (Player) e.getPlayer();
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent e) {
		Player p = (Player) e.getPlayer();
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			e.setCancelled(true);
		}
	}
}
