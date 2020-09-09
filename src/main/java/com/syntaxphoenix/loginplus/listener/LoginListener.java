package com.syntaxphoenix.loginplus.listener;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.DataTranslator;
import com.syntaxphoenix.loginplus.config.MYSQLConfig;
import com.syntaxphoenix.loginplus.config.MYSQLMethods;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.config.PasswordConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.utils.Account;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

@SuppressWarnings("deprecation")
public class LoginListener implements Listener {
	
	@EventHandler
	public void on(PlayerPreLoginEvent e) {
		String player = e.getName();
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.getName().toLowerCase().equalsIgnoreCase(player.toLowerCase())) {
				e.disallow(null, MessagesConfig.prefix + MessagesConfig.already_logged_in);
				break;
			}
		}
	}
	
	@EventHandler
	public void on(PlayerLoginEvent e) {
		String ip = e.getAddress().toString().substring(1 , e.getAddress().toString().length()).split(":")[0];
		if(PluginUtils.banned_ips.containsKey(ip)) {
			e.disallow(null, MessagesConfig.prefix + MessagesConfig.banned.replace("%BanTime%", PluginUtils.banned_ips.get(ip) + ""));
		}
		String uuid = e.getPlayer().getUniqueId().toString();
		if(MYSQLConfig.enabled) {
			Bukkit.getScheduler().runTaskAsynchronously(LoginPlus.m, new Runnable() {
				@Override
				public void run() {
					if(MYSQLMethods.isInMYSQLTable(uuid)) {
						Account ao;
						try {
							ao = new Account(uuid, MYSQLMethods.getPassword(uuid), EncryptionType.valueOf(MYSQLMethods.getHashType(uuid)), MYSQLMethods.isPremium(uuid));
							if (MYSQLMethods.isInMYSQLTable(uuid)) {
								DataTranslator.accounts.put(uuid, ao);
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}			
			});
		} else {
			if (PasswordConfig.isInDatabase(uuid)) {
				Account account = new Account(uuid, PasswordConfig.getHashedPassword(uuid), PasswordConfig.getHashtype(uuid), PasswordConfig.getPremium(uuid));
				DataTranslator.accounts.put(uuid, account);
			}
		}
	}
}
