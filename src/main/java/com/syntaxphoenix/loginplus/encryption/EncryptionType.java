package com.syntaxphoenix.loginplus.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.encryption.provider.*;

public enum EncryptionType {

    MD_5(MD5Provider.INSTANCE),
    SHA_256(SHA256Provider.INSTANCE),
    SHA_512(SHA512Provider.INSTANCE),
    ARGON_2(Argon2Provider.INSTANCE),
    BCRYPT(BCryptProvider.INSTANCE),
    SCRYPT(SCryptProvider.INSTANCE),
    PDKDF_2(Pdkdf2Provider.INSTANCE);

    private final EncryptionProvider provider;

    private EncryptionType(EncryptionProvider provider) {
        this.provider = provider;
    }

    public PasswordEncoder get(MainConfig config) {
        return provider.get(config);
    }

    public static EncryptionType fromString(String value) {
        for (EncryptionType type : EncryptionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
            if (type.name().replace("_", "").equalsIgnoreCase(value)) {
                return type;
            }
            if (type.name().replace("_", "-").equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}