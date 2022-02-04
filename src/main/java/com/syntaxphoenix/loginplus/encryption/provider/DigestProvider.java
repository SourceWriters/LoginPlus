package com.syntaxphoenix.loginplus.encryption.provider;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public abstract class DigestProvider extends EncryptionProvider {

    private final String algorithm;

    protected DigestProvider(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected final PasswordEncoder build(MainConfig config) {
        return new org.springframework.security.crypto.password.MessageDigestPasswordEncoder(algorithm);
    }

}
