package com.syntaxphoenix.loginplus.accounts.tasks;

import java.util.Optional;

import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.Account;

public class SyncSetLocalAccount extends BukkitRunnable {
	
	private String username;
	private Optional<Account> account;
	
	public SyncSetLocalAccount(String username, Optional<Account> account) {
		this.username = username;
		this.account = account;
	}

	@Override
	public void run() {
		LoginPlus.getInstance().getPluginUtils().getAccountManager().setLocalAccount(username, account);
	}

}
