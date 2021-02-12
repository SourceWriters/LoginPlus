package com.syntaxphoenix.loginplus.accounts.manager;

import java.util.HashMap;
import java.util.Optional;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class AccountManager {
	
	private HashMap<String, Optional<Account>> accounts;
	private PluginUtils pluginUtils;

	public AccountManager(PluginUtils pluginUtils) {
		this.accounts = new HashMap<String, Optional<Account>>();
		this.pluginUtils = pluginUtils;
	}

	public void clearLocalAccount(String username) {
		this.accounts.remove(username);
	}

	public boolean isLocalAccountLoaded(String username) {
		return this.accounts.containsKey(username);
	}

	public Optional<Account> getLocalAccount(String username) {
		if (this.accounts.containsKey(username)) {
			return this.accounts.get(username);
		}
		return Optional.ofNullable(null);
	}

	public void setLocalAccount(String username, Optional<Account> account) {
		this.accounts.put(username, account);
		this.pluginUtils.getLoginManager().callbackUser(username);
	}
}