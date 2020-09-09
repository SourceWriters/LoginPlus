package com.syntaxphoenix.loginplus.config;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.utils.Account;

public class DataTranslator {
	
	public static HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	public static void setPremium(String uuid, boolean premium) {
		if (MYSQLConfig.enabled == true) {
			if (accounts.containsKey(uuid)) {
				Account account = accounts.get(uuid);
				Bukkit.getScheduler().runTaskAsynchronously(LoginPlus.m, new Runnable() {
					@Override
					public void run() {
						try {
							MYSQLMethods.changeAccount(uuid, account.getPassword(), account.getHashType().toString(), premium);
						} catch (SQLException e) {							
							e.printStackTrace();
						}
					}			
				});
				account.setPremium(premium);
				accounts.put(uuid, account);
			} else {
				System.out.println("[LoginPlus] Error no Data found!");
			}
		} else {
			PasswordConfig.setPremium(uuid, premium);
		}
	}
	
	public static void setPassword(String uuid, String password, String hashtype) {
		if(MYSQLConfig.enabled == true) {
			if(accounts.containsKey(uuid)) {
				Account account = accounts.get(uuid);
				Bukkit.getScheduler().runTaskAsynchronously(LoginPlus.m, new Runnable() {
					@Override
					public void run() {
						try {
							MYSQLMethods.changeAccount(uuid, password, hashtype, account.isPremium());
						} catch (SQLException e) {							
							e.printStackTrace();
						}
					}			
				});
				account.setHashType(EncryptionType.valueOf(hashtype));
				account.setPassword(password);
				accounts.put(uuid, account);
			} else {
				System.out.println("[LoginPlus] Error no Data found!");
			}
		} else {
			PasswordConfig.setPassword(uuid, password, hashtype.toString());
		}
	}
	
	public static Account getAccountData(String uuid) {
		if(accounts.containsKey(uuid)) {
			return accounts.get(uuid);
		} else {
			return null;
		}
	}

}
