package com.syntaxphoenix.loginplus.commands;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.callback.ChangePasswordOthersCallback;
import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class ChangePasswordCommand implements CommandExecutor {
	
	private PluginUtils pluginUtils;
	
	public ChangePasswordCommand(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				if (player.hasPermission("loginplus.*") || player.hasPermission("loginplus.changepw")) {
					try {
						GeneralReflections.sendTitle(player, 20, 100, 20, MessagesConfig.title_changepw_title, MessagesConfig.title_changepw_subtitle);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException
							| NoSuchFieldException exception) {
						exception.printStackTrace();
					}
					pluginUtils.getUserHandler().setStatus(player, Status.CHANGEPW);
				} else {
					player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_permission);
				}
			} else if (args.length == 2) {
				if (player.hasPermission("loginplus.*")) {
					Player searchedPlayer = Bukkit.getPlayerExact(args[0]);
					if (searchedPlayer != null) {
						pluginUtils.getUserHandler().addAwaitingCallback(player);
						ChangePasswordOthersCallback callback = new ChangePasswordOthersCallback(pluginUtils, player, searchedPlayer, args[1]);
						callback.runTaskAsynchronously(LoginPlus.getInstance());
					} else {
						player.sendMessage(MessagesConfig.prefix + MessagesConfig.player_offline);
					}
				}
			} else {
				player.sendMessage(MessagesConfig.prefix + MessagesConfig.wrong_arguments);
			}
		} else {
			if (sender instanceof ConsoleCommandSender) {
				if (args.length == 2) {
					Player searchedPlayer = Bukkit.getPlayerExact(args[0]);
					if (searchedPlayer != null) {
						ChangePasswordOthersCallback callback = new ChangePasswordOthersCallback(pluginUtils, sender, searchedPlayer, args[1]);
						callback.runTaskAsynchronously(LoginPlus.getInstance());
					} else {
						System.out.println("[LoginPlus] The player is not online!");
					}					
				} else {
					System.out.println("[LoginPlus] Wrong arguments");
				}
			}
		}		
		return true;
	}
}
