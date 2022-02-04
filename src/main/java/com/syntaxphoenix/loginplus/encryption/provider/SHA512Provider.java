package com.syntaxphoenix.loginplus.encryption.provider;

import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class SHA512Provider extends DigestProvider {

    public static final EncryptionProvider INSTANCE = new SHA512Provider();

    private SHA512Provider() {
        super("SHA-512");
    }

}
