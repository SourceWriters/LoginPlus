package com.syntaxphoenix.loginplus.accounts.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.Config;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class ConfigAccountManager extends Config implements AccountManager {
	
	private HashMap<String, Optional<Account>> accounts;
	private PluginUtils pluginUtils;

	public ConfigAccountManager(PluginUtils pluginUtils) {
		super(new File("plugins/LoginPlus", "accounts.yml"));
		this.accounts = new HashMap<String, Optional<Account>>();
		this.pluginUtils = pluginUtils;
	}

	@Override
	public boolean hasAccount(String username) {
		if (this.accounts.containsKey(username) && this.accounts.get(username).isPresent()) {
			return true;
		}
		return this.configuration.contains("accounts." + username);
	}

	@Override
	public void createAccount(Account account) {
		if (!this.hasAccount(account.getUsername())) {
			this.loadAccount(account.getUsername(), Optional.of(account));
			this.setConfigAccount(account);
		}
	}

	@Override
	public Optional<Account> getAccount(String username) {
		Account account = null;
		if (accounts.containsKey(username) && this.accounts.get(username).isPresent()) {
			return this.accounts.get(username);
		} 
		if (this.hasAccount(username)) {
			String hash = set("accounts." + username + ".hash", "");
			// TODO: Default stuff
			EncryptionType type = EncryptionType.valueOf(set("accounts." + username + ".type", EncryptionType.ARGON_2.toString()));
			boolean premium = set("accounts." + username + ".premium", false);
			account = new Account(username, hash, type, premium);
		}
		loadAccount(username, Optional.ofNullable(account));
		return Optional.ofNullable(account);
	}

	@Override
	public void updateAccount(Account account) {
		this.accounts.put(account.getUsername(), Optional.of(account));
		this.setConfigAccount(account);
	}

	@Override
	public void clearLocalAccount(String username) {
		this.accounts.remove(username);
	}

	@Override
	public boolean isLocalAccountLoaded(String username) {
		return this.accounts.containsKey(username);
	}

	@Override
	public Optional<Account> getLocalAccount(String username) {
		if (this.accounts.containsKey(username)) {
			return this.accounts.get(username);
		}
		return Optional.ofNullable(null);
	}
	
	private void setConfigAccount(Account account) {
		this.configuration.set("accounts." + account.getUsername() + ".hash", account.getHash());
		this.configuration.set("accounts." + account.getUsername() + ".type", account.getType().toString());
		this.configuration.set("accounts." + account.getUsername() + ".premium", account.isPremium());
		save();
	}
	
	private void loadAccount(String username, Optional<Account> account) {
		this.accounts.put(username, account);
		this.pluginUtils.getLoginManager().callbackUser(username);
	}

}
