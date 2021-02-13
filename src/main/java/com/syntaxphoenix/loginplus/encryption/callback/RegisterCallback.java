package com.syntaxphoenix.loginplus.encryption.callback;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.encryption.thread.EncryptionCallback;
import com.syntaxphoenix.loginplus.listener.InventoryClearListener;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.versiontools.reflection.GeneralReflections;

public class RegisterCallback extends BukkitRunnable implements EncryptionCallback {
	
	private PluginUtils pluginUtils;
	
	private Player player;
	private String password;
	private EncryptionType type;
	
	public RegisterCallback(PluginUtils pluginUtils, Player player, String password) {
		this.pluginUtils = pluginUtils;
		this.player = player;
		this.password = password;
		
		this.type = this.pluginUtils.getConfig().getEncryptionType();
	}

	@Override
	public void run() {
		this.pluginUtils.getEncryptionManager().hashPassword(password, type, this);
	}

	@Override
	public void validateCallback(boolean validation) {
		// Unused - TODO: Improve Interface
	}

	@Override
	public void encryptCallback(String hash) {
		Account account = new Account(player.getName(), hash, type, false);
		try {
			pluginUtils.createAccountDatabase().createAccount(account);
			try {
				GeneralReflections.sendTitle(player, 20, pluginUtils.getConfig().getTitleTime() * 20, 20, MessagesConfig.title_register_success_title, MessagesConfig.title_register_success_subtitle);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException exception2) {
				exception2.printStackTrace();
			}
			pluginUtils.getUserHandler().removeStatus(player);
			InventoryClearListener.setInventory(player);
		} catch (Exception exception) {
			// TODO: Proper handling here
			exception.printStackTrace();
		}
		pluginUtils.getUserHandler().removeAwaitingCallback(player);
	}
}
