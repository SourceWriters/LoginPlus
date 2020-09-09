package com.syntaxphoenix.loginplus.listener;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.DataTranslator;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;
import com.syntaxphoenix.loginplus.utils.CaptchaUtils;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class JoinListener implements Listener {
	
	@EventHandler
	public void on(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PluginUtils.captcha.add(p);
		String uuid = p.getUniqueId().toString();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.m, new Runnable() {
			@Override
			public void run() {
				PluginUtils.captcha.remove(p);
				if(DataTranslator.accounts.containsKey(uuid)) {
					if(MainConfig.captcha == true && MainConfig.captcha_on_login) {
						PluginUtils.captcha.add(p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.m, new Runnable() {
							@Override
							public void run() {
								p.openInventory(CaptchaUtils.createCaptchaInventory(p));
							}		
						}, 1);
					} else {
						if(DataTranslator.getAccountData(uuid).isPremium()) {
							if(!PremiumCheck.isPremium(p)) {
								p.kickPlayer(MessagesConfig.prefix + MessagesConfig.no_premium);
							}
							PluginUtils.login.remove(p);
							PluginUtils.timer.remove(p);
						} else {
							try {
								GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_login_title, MessagesConfig.title_login_subtitle);
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException
									| NoSuchFieldException e) {
								e.printStackTrace();
							}
							PluginUtils.login.add(p);
							if(MainConfig.timer_enabled) {
								PluginUtils.timer.put(p, 0);
							}
							PluginUtils.attempts.put(p, MainConfig.login_attempts);
						}
					}
				} else {
					if(MainConfig.captcha == true && MainConfig.captcha_on_register) {
						PluginUtils.captcha.add(p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.m, new Runnable() {
							@Override
							public void run() {
								p.openInventory(CaptchaUtils.createCaptchaInventory(p));
							}		
						}, 1);
					} else {
						try {
							GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_register_title, MessagesConfig.title_register_subtitle);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException
								| NoSuchFieldException e) {
							e.printStackTrace();
						}
						PluginUtils.register.add(p);
					}
				}
			}			
		});
	}
}
