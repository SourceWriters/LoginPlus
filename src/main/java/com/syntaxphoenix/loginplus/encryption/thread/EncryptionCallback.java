package com.syntaxphoenix.loginplus.encryption.thread;

public interface EncryptionCallback {
	
	public void validateCallback(boolean validation);
	
	public void encryptCallback(String hash);

}
