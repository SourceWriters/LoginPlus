package com.syntaxphoenix.loginplus.accounts.manager;

import java.util.Optional;

import com.syntaxphoenix.loginplus.accounts.Account;

public interface AccountManager {
	
	public boolean hasAccount(String username) throws Exception;
	
	public void createAccount(Account account) throws Exception;
	
	public void updateAccount(Account account) throws Exception;
	
	public Optional<Account> getAccount(String username) throws Exception;
	
	public Optional<Account> getLocalAccount(String username);
	
	public boolean isLocalAccountLoaded(String username);
	
	public void clearLocalAccount(String username);

}
