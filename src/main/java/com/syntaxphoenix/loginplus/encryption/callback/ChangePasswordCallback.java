package com.syntaxphoenix.loginplus.encryption.callback;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.accounts.Account;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionType;
import com.syntaxphoenix.loginplus.encryption.thread.EncryptionCallback;
import com.syntaxphoenix.loginplus.utils.PlayerHelper;
import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class ChangePasswordCallback extends BukkitRunnable implements EncryptionCallback {

    private PluginUtils pluginUtils;

    private Player player;
    private String password;
    private EncryptionType type;

    public ChangePasswordCallback(PluginUtils pluginUtils, Player player, String password) {
        this.pluginUtils = pluginUtils;
        this.player = player;
        this.password = password;

        this.type = pluginUtils.getConfig().getEncryptionType();
    }

    @Override
    public void run() {
        this.pluginUtils.getEncryptionManager().hashPassword(password, type, this);
    }

    @Override
    public void validateCallback(boolean validation) {

    }

    @Override
    public void encryptCallback(String hash) {
        Optional<Account> account;
        try {
            account = this.pluginUtils.getAccountManager().getLocalAccount(player.getName());
            if (!account.isPresent()) {
                account = this.pluginUtils.createAccountDatabase().getAccount(player.getName());
            }

            account.get().setType(type);
            account.get().setHash(hash);
            this.pluginUtils.createAccountDatabase().updateAccount(account.get());

            PlayerHelper.sendTitle(player, 20, this.pluginUtils.getConfig().getTitleTime() * 20, 20,
                MessagesConfig.title_changepw_success_title, MessagesConfig.title_changepw_success_subtitle);
            this.pluginUtils.getUserHandler().removeStatus(player);
        } catch (Exception exception) {
            // TODO: Proper handling here
            exception.printStackTrace();
        }
        this.pluginUtils.getUserHandler().removeAwaitingCallback(player);
    }

}
