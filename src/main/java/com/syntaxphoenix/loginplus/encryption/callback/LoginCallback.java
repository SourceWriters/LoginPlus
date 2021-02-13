package com.syntaxphoenix.loginplus.encryption.callback;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.thread.EncryptionCallback;
import com.syntaxphoenix.loginplus.listener.InventoryClearListener;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class LoginCallback extends BukkitRunnable implements EncryptionCallback {
	
	private String password;
	private Player player;
	
	private PluginUtils pluginUtils;
	
	public LoginCallback(PluginUtils pluginUtils, Player player, String password) {
		this.pluginUtils = pluginUtils;
		
		this.password = password;
		this.player = player;
	}

	@Override
	public void run() {
		try {
			Optional<Account> account = pluginUtils.createAccountDatabase().getAccount(this.player.getName());
			if (account.isPresent()) {
				this.pluginUtils.getEncryptionManager().validatePassword(password, account.get().getType(), account.get().getHash(), this);
			}
			// TODO: Handle else
		} catch (Exception exception) {
			// TODO: Handle crash here
			exception.printStackTrace();
		}
	}

	@Override
	public void validateCallback(boolean validation) {
		MainConfig config = pluginUtils.getConfig();
		if (validation) {
			pluginUtils.getUserHandler().removeStatus(player);
			pluginUtils.getTimer().removeTimerPlayer(player);
			InventoryClearListener.setInventory(player);
			try {
				GeneralReflections.sendTitle(player, 20, config.getTitleTime() * 20, 20, MessagesConfig.title_login_success_title, MessagesConfig.title_login_success_subtitle);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| NoSuchFieldException exception) {
				exception.printStackTrace();
			}
		} else {
			if (this.pluginUtils.getLoginManager().tooManyAttempts(player)) {
				pluginUtils.getLoginManager().clearAttempts(player);
				pluginUtils.getTimer().addKickPlayer(player);
				String ip = player.getAddress().getAddress().toString().replace("/", "");
				if (config.isLoginFailedBan()) {
					this.pluginUtils.getTimer().addBannedIp(ip);
				}
			} else {
				int triesLeft = pluginUtils.getConfig().getMaxLoginAttempts() - pluginUtils.getLoginManager().getLoginAttempts(player);
				try {
					GeneralReflections.sendTitle(
						player,
						20,
						config.getTitleTime() * 20,
						20,
						MessagesConfig.title_login_failed_title,
						MessagesConfig.title_login_failed_subtitle.replace("%Attempts%", triesLeft + "")
					);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| NoSuchFieldException exception) {
					exception.printStackTrace();
				}
			}
		}
		pluginUtils.getUserHandler().removeAwaitingCallback(player);
	}

	@Override
	public void encryptCallback(String hash) {
		// Not used here - TODO: Improve interface
	}

}
