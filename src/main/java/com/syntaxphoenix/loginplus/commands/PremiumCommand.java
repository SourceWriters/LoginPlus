package com.syntaxphoenix.loginplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.config.DataTranslator;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.premium.PremiumCheck;

public class PremiumCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {	
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String uuid = player.getUniqueId().toString();
			if (player.hasPermission("loginplus.*") || player.hasPermission("loginplus.premium")) {
				if (DataTranslator.getAccountData(uuid).isPremium() == false) {
					if (PremiumCheck.isPremium(player)) {
						DataTranslator.setPremium(uuid, true);
						player.sendMessage(MessagesConfig.prefix + MessagesConfig.premium_enabled);
					} else {
						player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_premium);
					}
				} else {
					DataTranslator.setPremium(uuid, false);
					player.sendMessage(MessagesConfig.prefix + MessagesConfig.premium_disabled);
				}
			} else {
				player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_permission);
			}
		} else {
			System.out.println("[LoginPlus] This Command can only be executed by Players");
		}
		
		return true;
	}

}