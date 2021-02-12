package com.syntaxphoenix.loginplus.accounts.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Optional;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.mysql.Mysql;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class MysqlAccountManager implements AccountManager {
	
	private Mysql mysql;
	private HashMap<String, Optional<Account>> accounts;
	private PluginUtils pluginUtils;
	
	public MysqlAccountManager(Mysql mysql, PluginUtils pluginUtils) {
		this.mysql = mysql;
		this.accounts = new HashMap<String, Optional<Account>>();
		this.pluginUtils = pluginUtils;
	}

	@Override
	public boolean hasAccount(String username) throws Exception {
		if (this.accounts.containsKey(username) && this.accounts.get(username).isPresent()) {
			return true;
		}
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"SELECT * FROM `accounts` WHERE `username`= ?"
		);
		statement.setString(1, username);
		ResultSet resultSet = statement.executeQuery();
		
		while (resultSet.next()) {			
			if (resultSet != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void createAccount(Account account) throws Exception {
		this.loadAccount(account.getUsername(), Optional.ofNullable(account));
		
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"INSERT INTO `accounts`(`username`,`hash`,`type`,`premium`) VALUES (?,?,?,?)"
		);
		statement.setString(1, account.getUsername());
		statement.setString(2, account.getHash());
		statement.setString(3, account.getType().toString());
		statement.setBoolean(4, account.isPremium());
		statement.execute();
	}

	@Override
	public void updateAccount(Account account) throws Exception {
		this.accounts.put(account.getUsername(), Optional.ofNullable(account));
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"UPDATE `accounts` SET `hash` = ?, `type` = ?, `premium` = ? WHERE `username` = ?"
		);
		statement.setString(1, account.getHash());
		statement.setString(2, account.getType().toString());
		statement.setBoolean(3, account.isPremium());
		statement.setString(4, account.getUsername());
		statement.execute();
	}

	@Override
	public Optional<Account> getAccount(String username) throws Exception {
		if (this.accounts.containsKey(username) && this.accounts.get(username).isPresent()) {
			return this.accounts.get(username);
		}
		Account account = null;
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"SELECT * FROM `accounts` WHERE `username`= ?"
		);
		statement.setString(1, username);
		ResultSet resultSet = statement.executeQuery();
		
		while (resultSet.next()) {			
			if (resultSet != null) {
				String hash = resultSet.getString("hash");
				EncryptionType type = EncryptionType.valueOf(resultSet.getString("hash"));
				boolean premium = resultSet.getBoolean("premium");
				account = new Account(username, hash, type, premium);
			}
		}
		this.loadAccount(username, Optional.ofNullable(account));
		return Optional.ofNullable(account);
	}

	@Override
	public void clearLocalAccount(String username) {
		this.accounts.remove(username);
	}

	@Override
	public boolean isLocalAccountLoaded(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Account> getLocalAccount(String username) {
		if (this.accounts.containsKey(username)) {
			return this.accounts.get(username);
		}
		return Optional.ofNullable(null);
	}
	
	private void loadAccount(String username, Optional<Account> account) {
		this.accounts.put(username, account);
		this.pluginUtils.getLoginManager().callbackUser(username);
	}

}
