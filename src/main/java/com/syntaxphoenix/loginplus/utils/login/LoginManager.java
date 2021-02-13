package com.syntaxphoenix.loginplus.utils.login;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class LoginManager {
	
	private PluginUtils pluginUtils;
	private HashMap<String, LoginCallback> callbacks;
	private HashMap<Player, Integer> attempts;
	
	public LoginManager(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
		this.callbacks = new HashMap<String, LoginCallback>();
		this.attempts = new HashMap<Player, Integer>();
	}
	
	public void loginUser(Player player, boolean checkCaptcha) {
		LoginCallback loginCallback = new LoginCallback(this.pluginUtils, player, checkCaptcha);	
		System.out.println("Creating callback");
		if (this.pluginUtils.getAccountManager().isLocalAccountLoaded(player.getName())) {
			loginCallback.handleLogin();
		} else {
			this.callbacks.put(player.getName(), loginCallback);
		}
	}
	
	public void callbackUser(String username) {
		System.out.println("Calling back on user" + username);
		if (this.callbacks.containsKey(username)) {
			this.callbacks.get(username).handleLogin();
			this.callbacks.remove(username);
		}
	}
	
	public boolean tooManyAttempts(Player player) {
		if (!attempts.containsKey(player)) {
			return false;
		}
		return attempts.get(player) >= pluginUtils.getConfig().getMaxLoginAttempts();
	}
	
	public void addLoginAttempt(Player player) {
		int tries = attempts.containsKey(player) ? (this.attempts.get(player) + 1) : 1;
		this.attempts.put(player, tries);
	}
	
	public void clearAttempts(Player player) {
		this.attempts.remove(player);
	}
	
	public int getLoginAttempts(Player player) {
		return attempts.containsKey(player) ? this.attempts.get(player) : 0;
	}

}
