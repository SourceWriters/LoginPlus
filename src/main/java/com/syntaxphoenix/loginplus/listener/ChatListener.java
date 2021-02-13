package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.encryption.callback.ChangePasswordCallback;
import com.syntaxphoenix.loginplus.encryption.callback.LoginCallback;
import com.syntaxphoenix.loginplus.encryption.callback.RegisterCallback;
import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class ChatListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public ChatListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler
	public void on(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (pluginUtils.getUserHandler().hasStatus(player)) {
			Status status = pluginUtils.getUserHandler().getStatus(player);
			event.setCancelled(true);

			if (!pluginUtils.getUserHandler().isAwaitingCallback(player)) {
				String password = event.getMessage();
				if (status == Status.LOGIN) {
					pluginUtils.getUserHandler().addAwaitingCallback(player);
					LoginCallback callback = new LoginCallback(pluginUtils, player, password);
					callback.runTaskAsynchronously(LoginPlus.getInstance());
				} else if (status == Status.REGISTER) {
					pluginUtils.getUserHandler().addAwaitingCallback(player);
					RegisterCallback callback = new RegisterCallback(pluginUtils, player, password);
					callback.runTaskAsynchronously(LoginPlus.getInstance());
				} else if (status == Status.CHANGEPW) {
					pluginUtils.getUserHandler().addAwaitingCallback(player);
					ChangePasswordCallback callback = new ChangePasswordCallback(pluginUtils, player, password);
					callback.runTaskAsynchronously(LoginPlus.getInstance());
				}
			}
		}
	}
}
