package com.syntaxphoenix.loginplus.accounts.database;

import java.util.Optional;

import com.syntaxphoenix.loginplus.accounts.Account;

public interface AccountDatabase {
	
	public boolean hasAccount(String username) throws Exception;
	
	public void createAccount(Account account) throws Exception;
	
	public void updateAccount(Account account) throws Exception;
	
	public Optional<Account> getAccount(String username) throws Exception;

}
