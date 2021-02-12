package com.syntaxphoenix.loginplus.utils.tasks;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class ChangePremiumTask extends BukkitRunnable {
	
	private PluginUtils pluginUtils;
	private Player player;
	
	public ChangePremiumTask(PluginUtils pluginUtils, Player player) {
		this.pluginUtils = pluginUtils;
		this.player = player;
	}

	@Override
	public void run() {
		try {
			Optional<Account> accountOptional = pluginUtils.getAccountManager().getLocalAccount(player.getName());
			if (!accountOptional.isPresent()) {
				accountOptional = pluginUtils.createAccountDatabase().getAccount(player.getName());
				return;
			}
			Account account = accountOptional.get();
			if (PremiumCheck.isPremium(player)) {
				player.sendMessage(MessagesConfig.prefix + MessagesConfig.premium_enabled);
			} else {
				player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_premium);
			}
			account.setPremium(!account.isPremium());
			pluginUtils.createAccountDatabase().updateAccount(account);
		} catch (Exception exception) {
			// TODO: Proper message here
			exception.printStackTrace();
		}
	}

}
