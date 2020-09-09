package com.syntaxphoenix.loginplus.commands;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.config.DataTranslator;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionUtils;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class ChangePasswordCommand implements CommandExecutor {

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
							| NoSuchFieldException e) {
						e.printStackTrace();
					}
					PluginUtils.changepw.add(player);
				} else {
					player.sendMessage(MessagesConfig.prefix + MessagesConfig.no_permission);
				}
			} else if (args.length == 2) {
				if (player.hasPermission("loginplus.*")) {
					Player searchedPlayer = Bukkit.getPlayerExact(args[0]);
					if (searchedPlayer != null) {
						DataTranslator.setPassword(searchedPlayer.getUniqueId().toString(), EncryptionUtils.hashPassword(args[1], MainConfig.type), MainConfig.type.toString());
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
						DataTranslator.setPassword(searchedPlayer.getUniqueId().toString(), EncryptionUtils.hashPassword(args[1], MainConfig.type), MainConfig.type.toString());
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
