package com.syntaxphoenix.loginplus.encryption.provider;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class Argon2Provider extends EncryptionProvider {

    public static final EncryptionProvider INSTANCE = new Argon2Provider();

    private Argon2Provider() {}

    @Override
    protected PasswordEncoder build(MainConfig config) {
        return new Argon2PasswordEncoder(16, 32, config.getArgon2Parallelism(), config.getArgon2Memory(), config.getArgon2Iterations());
    }

}
