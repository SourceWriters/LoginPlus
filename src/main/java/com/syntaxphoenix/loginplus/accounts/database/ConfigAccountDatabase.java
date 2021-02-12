package com.syntaxphoenix.loginplus.accounts.database;

import java.io.File;
import java.util.Optional;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.accounts.tasks.SyncSetLocalAccount;
import com.syntaxphoenix.loginplus.config.Config;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;

public class ConfigAccountDatabase extends Config implements AccountDatabase {

	public ConfigAccountDatabase() {
		super(new File("plugins/LoginPlus", "accounts.yml"));
	}

	@Override
	public boolean hasAccount(String username) throws Exception {
		return this.configuration.contains("accounts." + username);
	}

	@Override
	public void createAccount(Account account) throws Exception {
		this.setConfigAccount(account);
		new SyncSetLocalAccount(account.getUsername(), Optional.of(account)).runTaskLater(LoginPlus.getInstance(), 1);
	}

	@Override
	public void updateAccount(Account account) throws Exception {
		this.setConfigAccount(account);
		new SyncSetLocalAccount(account.getUsername(), Optional.of(account)).runTaskLater(LoginPlus.getInstance(), 1);	
	}
	
	private void setConfigAccount(Account account) {
		this.configuration.set("accounts." + account.getUsername() + ".hash", account.getHash());
		this.configuration.set("accounts." + account.getUsername() + ".type", account.getType().toString());
		this.configuration.set("accounts." + account.getUsername() + ".premium", account.isPremium());
		save();
	}

	@Override
	public Optional<Account> getAccount(String username) throws Exception {
		Account account = null;
		if (this.hasAccount(username)) {
			String hash = this.configuration.getString("accounts." + username + ".hash");
			EncryptionType type = EncryptionType.valueOf(this.configuration.getString("accounts." + username + ".type"));
			boolean premium = this.configuration.getBoolean("accounts." + username + ".premium");
			account = new Account(username, hash, type, premium);
		}
		new SyncSetLocalAccount(username, Optional.ofNullable(account)).runTaskLater(LoginPlus.getInstance(), 1);	
		return Optional.ofNullable(account);
	}

}
