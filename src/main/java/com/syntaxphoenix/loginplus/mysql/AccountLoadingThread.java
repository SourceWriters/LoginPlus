package com.syntaxphoenix.loginplus.mysql;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class AccountLoadingThread extends Thread {
	
	private PluginUtils pluginUtils;
	private String username;
	
	public AccountLoadingThread(PluginUtils pluginUtils, String username) {
		this.pluginUtils = pluginUtils;
		this.username = username;
	}
	
	public void run() {
		try {
			pluginUtils.createAccountDatabase().getAccount(username);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
