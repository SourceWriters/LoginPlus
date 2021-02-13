package com.syntaxphoenix.loginplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.tasks.ChangePremiumTask;

public class PremiumCommand implements CommandExecutor {
	
	private PluginUtils pluginUtils;
	
	public PremiumCommand(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {	
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("loginplus.*") || player.hasPermission("loginplus.premium")) {
				ChangePremiumTask task = new ChangePremiumTask(pluginUtils, player);
				task.runTaskAsynchronously(LoginPlus.getInstance());
			} else {
				player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_permission);
			}
		} else {
			System.out.println("[LoginPlus] This Command can only be executed by Players");
		}
		
		return true;
	}

}