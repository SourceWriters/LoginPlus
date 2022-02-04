package com.syntaxphoenix.loginplus.encryption.thread;

import org.bukkit.Bukkit;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.encryption.EncryptionManager;

public class EncryptionThread extends Thread {

    private EncryptionThreadMethod method;
    private EncryptionManager manager;
    private EncryptionCallback callback;

    private String password;
    private String hash;

    private PasswordEncoder encoder;

    public EncryptionThread(EncryptionManager manager, EncryptionCallback callback, String password, PasswordEncoder encoder) {
        this.method = EncryptionThreadMethod.HASH;
        this.manager = manager;
        this.callback = callback;

        this.password = password;
        this.encoder = encoder;
    }

    public EncryptionThread(EncryptionManager manager, EncryptionCallback callback, String password, PasswordEncoder encoder, String hash) {
        this.method = EncryptionThreadMethod.VALIDATE;
        this.manager = manager;
        this.callback = callback;

        this.password = password;
        this.encoder = encoder;
        this.hash = hash;
    }

    public void run() {
        this.checkQueue();
        try {
            long freeMemory = Runtime.getRuntime().freeMemory();
            while (freeMemory < ((manager.getConfig().getArgon2Memory() + 10) * 1024 * 1024)) {
                Thread.sleep(10);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            exception.printStackTrace();
        }
        if (method == EncryptionThreadMethod.HASH) {
            this.hashPassword();
        } else {
            this.validatePassword();
        }
    }

    public synchronized void checkQueue() {
        if (!manager.canRun(this)) {
            try {
                wait();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                exception.printStackTrace();
            }
        }
    }

    private void hashPassword() {
        String hash = encoder.encode(this.password);
        Bukkit.getScheduler().runTask(LoginPlus.getInstance(), new Runnable() {
            @Override
            public void run() {
                callback.encryptCallback(hash);
                manager.endRun();
            }
        });
    }

    private void validatePassword() {
        boolean valid = encoder.matches(this.password, this.hash);
        Bukkit.getScheduler().runTask(LoginPlus.getInstance(), new Runnable() {
            @Override
            public void run() {
                callback.validateCallback(valid);
                manager.endRun();
            }
        });
    }

}
