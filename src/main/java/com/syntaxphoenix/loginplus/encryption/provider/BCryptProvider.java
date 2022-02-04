package com.syntaxphoenix.loginplus.encryption.provider;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class BCryptProvider extends EncryptionProvider {
    
    public static final EncryptionProvider INSTANCE = new BCryptProvider();
    
    private BCryptProvider() {}

    @Override
    protected PasswordEncoder build(MainConfig config) {
        return new BCryptPasswordEncoder(config.getBcryptStrength());
    }

}
