package com.syntaxphoenix.loginplus.listener;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.syntaxphoenix.loginplus.config.DataTranslator;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.config.PasswordConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.encryption.EncryptionUtils;
import com.syntaxphoenix.loginplus.utils.Account;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class ChatListener implements Listener {
	
	@EventHandler
	public void on(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();
		
		if (PluginUtils.login.contains(p)) {
			e.setCancelled(true);
			String password = e.getMessage();
			EncryptionType type = PasswordConfig.getHashtype(p.getUniqueId().toString());
			if (EncryptionUtils.verifyPassword(password, type, DataTranslator.getAccountData(uuid).getPassword())) {
				PluginUtils.login.remove(p);
				PluginUtils.timer.remove(p);
				InventoryClearListener.setInventory(p);
				try {
					GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_login_success_title, MessagesConfig.title_login_success_subtitle);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| NoSuchFieldException e1) {
					e1.printStackTrace();
				}
			} else {
				PluginUtils.attempts.put(p, PluginUtils.attempts.get(p)-1);
				if(PluginUtils.attempts.get(p) <= 0) {
					PluginUtils.attempts.remove(p);
					PluginUtils.kick.add(p);
					String ip = p.getAddress().getAddress().toString().replace("/", "");
					if(MainConfig.login_failed_ban == true) {
						PluginUtils.banned_ips.put(ip, MainConfig.login_failed_ban_time);
					}
				} else {
					try {
						GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_login_failed_title, MessagesConfig.title_login_failed_subtitle.replace("%Attempts%", PluginUtils.attempts.get(p) + ""));
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException
							| NoSuchFieldException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else if (PluginUtils.register.contains(p)) {
			e.setCancelled(true);
			String password = e.getMessage();
			Account ao = new Account(uuid, "password", MainConfig.type, false);
			DataTranslator.accounts.put(uuid, ao);
			DataTranslator.setPassword(uuid, EncryptionUtils.hashPassword(password, MainConfig.type), MainConfig.type.toString());
			try {
				GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_register_success_title, MessagesConfig.title_register_success_subtitle);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
				e1.printStackTrace();
			}
			PluginUtils.register.remove(p);
			InventoryClearListener.setInventory(p);
		} else if(PluginUtils.changepw.contains(p)) {
			e.setCancelled(true);
			String password = e.getMessage();
			DataTranslator.setPassword(uuid, EncryptionUtils.hashPassword(password, MainConfig.type), MainConfig.type.toString());
			try {
				GeneralReflections.sendTitle(p, 20, 100, 20, MessagesConfig.title_changepw_success_title, MessagesConfig.title_changepw_success_subtitle);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
				e1.printStackTrace();
			}
			PluginUtils.changepw.remove(p);
		} else if(PluginUtils.captcha.contains(p)) {
			e.setCancelled(true);
		}
	}
}
