package com.syntaxphoenix.loginplus.encryption.provider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class SCryptProvider extends EncryptionProvider {
    
    public static final EncryptionProvider INSTANCE = new SCryptProvider();
    
    private SCryptProvider() {}

    @Override
    protected PasswordEncoder build(MainConfig config) {
        return new SCryptPasswordEncoder(16384, 8, config.getScryptParallelism(), 32, 64);
    }

}
