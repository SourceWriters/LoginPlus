package com.syntaxphoenix.loginplus.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;

public abstract class EncryptionProvider {

    private PasswordEncoder encoder = null;

    protected abstract PasswordEncoder build(MainConfig config);

    public final PasswordEncoder get(MainConfig config) {
        if (encoder != null) {
            return encoder;
        }
        return (encoder = build(config));
    }

    public final void reset() {
        encoder = null;
    }

}
