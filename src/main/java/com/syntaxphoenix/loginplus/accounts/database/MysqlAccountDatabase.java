package com.syntaxphoenix.loginplus.accounts.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.accounts.tasks.SyncSetLocalAccount;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.mysql.Mysql;

public class MysqlAccountDatabase implements AccountDatabase {
	
	private Mysql mysql;
	
	public MysqlAccountDatabase(Mysql mysql) {
		this.mysql = mysql;
	}

	@Override
	public boolean hasAccount(String username) throws Exception {
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
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"INSERT INTO `accounts`(`username`,`hash`,`type`,`premium`) VALUES (?,?,?,?)"
		);
		statement.setString(1, account.getUsername());
		statement.setString(2, account.getHash());
		statement.setString(3, account.getType().toString());
		statement.setBoolean(4, account.isPremium());
		statement.execute();
		
		new SyncSetLocalAccount(account.getUsername(), Optional.of(account)).runTaskLater(LoginPlus.getInstance(), 1);	
	}

	@Override
	public void updateAccount(Account account) throws Exception {
		PreparedStatement statement = this.mysql.getConnection().prepareStatement(
			"UPDATE `accounts` SET `hash` = ?, `type` = ?, `premium` = ? WHERE `username` = ?"
		);
		statement.setString(1, account.getHash());
		statement.setString(2, account.getType().toString());
		statement.setBoolean(3, account.isPremium());
		statement.setString(4, account.getUsername());
		statement.execute();
		
		new SyncSetLocalAccount(account.getUsername(), Optional.of(account)).runTaskLater(LoginPlus.getInstance(), 1);	
	}

	@Override
	public Optional<Account> getAccount(String username) throws Exception {
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
		new SyncSetLocalAccount(username, Optional.ofNullable(account)).runTaskLater(LoginPlus.getInstance(), 1);
		return Optional.ofNullable(account);
	}

}
