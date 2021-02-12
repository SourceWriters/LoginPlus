package com.syntaxphoenix.loginplus.utils.login;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;
import com.syntaxphoenix.loginplus.utils.CaptchaUtils;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class LoginCallback extends Thread {
	
	private PluginUtils pluginUtils;
	private Player player;
	private boolean checkCaptcha;
	
	public LoginCallback(PluginUtils pluginUtils, Player player, boolean checkCaptcha) {
		this.pluginUtils = pluginUtils;
		this.player = player;
		this.checkCaptcha = checkCaptcha;
	}
	
	public void run() {
		MainConfig config = pluginUtils.getConfig();
		Optional<Account> account = this.pluginUtils.getAccountManager().getLocalAccount(player.getName());
		if (account.isPresent()) {
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
					this.pluginUtils.getLoginManager().addLoginAttempt(player);
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
		Bukkit.getScheduler().scheduleSyncDelayedTask(LoginPlus.getInstance(), new Runnable() {
			@Override
			public void run() {
				pluginUtils.getUserHandler().setStatus(player, Status.CAPTCHA);
				CaptchaUtils.openInventory(player);
			}		
		}, 1);
	}

}
