package com.syntaxphoenix.loginplus.utils.login;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.listener.InventoryClearListener;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;
import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.captcha.CaptchaInventoryHolder;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class LoginCallback {
	
	private PluginUtils pluginUtils;
	private Player player;
	private boolean checkCaptcha;
	
	public LoginCallback(PluginUtils pluginUtils, Player player, boolean checkCaptcha) {
		this.pluginUtils = pluginUtils;
		this.player = player;
		this.checkCaptcha = checkCaptcha;
	}
	
	public void handleLogin() {
		MainConfig config = pluginUtils.getConfig();
		Optional<Account> account = this.pluginUtils.getAccountManager().getLocalAccount(player.getName());
		if (account.isPresent()) {	
			if (config.isSessionsEnabled()) { // Handle Session-Logic
				String ip = player.getAddress().toString().substring(1 , player.getAddress().toString().length()).split(":")[0];	
				if (pluginUtils.getSessionManager().validateSession(ip, player.getName())) {
					pluginUtils.getSessionManager().resetSession(ip);
					pluginUtils.getUserHandler().removeStatus(player);
					InventoryClearListener.setInventory(player);
					try {
						GeneralReflections.sendTitle(player, 20, config.getTitleTime() * 20, 20, MessagesConfig.title_login_session_title, MessagesConfig.title_login_session_subtitle);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException
							| NoSuchFieldException exception) {
						exception.printStackTrace();
					}
					return;
				}
				pluginUtils.getSessionManager().resetSession(ip);
			}
			
			if (config.isCaptchaEnabled() && config.isCaptchaOnLogin() && this.checkCaptcha) {
				this.openCaptchaMenu();
			} else {
				if (account.get().isPremium()) {
					if (!PremiumCheck.isPremium(player)) {
						player.kickPlayer(MessagesConfig.prefix + MessagesConfig.no_premium);
						return;
					}
					pluginUtils.getUserHandler().removeStatus(player);
					pluginUtils.getTimer().removeTimerPlayer(player);
				} else {
					try {
						GeneralReflections.sendTitle(player, 20, 100, 20, MessagesConfig.title_login_title, MessagesConfig.title_login_subtitle);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException
							| NoSuchFieldException exception) {
						exception.printStackTrace();
					}
					pluginUtils.getUserHandler().setStatus(player, Status.LOGIN);
					if (pluginUtils.getConfig().isTimerEnabled()) {
						pluginUtils.getTimer().addTimerPlayer(player);
					}
				}
			}
		} else {
			if (config.isCaptchaEnabled() && config.isCaptchaOnRegister() && this.checkCaptcha) {
				this.openCaptchaMenu();
			} else {
				try {
					GeneralReflections.sendTitle(player, 20, 100, 20, MessagesConfig.title_register_title, MessagesConfig.title_register_subtitle);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| NoSuchFieldException exception) {
					exception.printStackTrace();
				}
				pluginUtils.getUserHandler().setStatus(player, Status.REGISTER);
			}
		}
	}
	
	private void openCaptchaMenu() {
		pluginUtils.getUserHandler().setStatus(player, Status.CAPTCHA);
		Inventory inventory = new CaptchaInventoryHolder(player).getInventory();
		Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.openInventory(inventory);
			}	
		}, 1);
	}

}
