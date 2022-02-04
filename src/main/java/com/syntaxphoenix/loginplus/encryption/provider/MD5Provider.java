package com.syntaxphoenix.loginplus.encryption.provider;

import com.syntaxphoenix.loginplus.encryption.EncryptionProvider;

public final class MD5Provider extends DigestProvider {

    public static final EncryptionProvider INSTANCE = new MD5Provider();

    private MD5Provider() {
        super("MD5");
    }

}
