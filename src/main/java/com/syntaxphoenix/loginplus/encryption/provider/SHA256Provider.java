package com.syntaxphoenix.loginplus.encryption.provider;

import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class SHA256Provider extends DigestProvider {

    public static final EncryptionProvider INSTANCE = new SHA256Provider();

    private SHA256Provider() {
        super("SHA-256");
    }

}
