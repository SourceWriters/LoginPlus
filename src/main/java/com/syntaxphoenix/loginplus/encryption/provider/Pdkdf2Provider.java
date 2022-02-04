package com.syntaxphoenix.loginplus.encryption.provider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class Pdkdf2Provider extends EncryptionProvider {
    
    public static final EncryptionProvider INSTANCE = new Pdkdf2Provider();
    
    private Pdkdf2Provider() {}

    @Override
    protected PasswordEncoder build(MainConfig config) {
        return new Pbkdf2PasswordEncoder(config.getPdkdf2Secret(), 8, config.getPdkdf2Iterations(), 256);
    }

}
