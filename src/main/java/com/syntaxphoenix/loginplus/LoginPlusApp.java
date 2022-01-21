package com.syntaxphoenix.loginplus;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

import net.sourcewriters.minecraft.vcompat.updater.CompatApp;
import net.sourcewriters.minecraft.vcompat.updater.Reason;

final class LoginPlusApp extends CompatApp {

    private final Logger bukkitLogger;
    private final PluginUtils utils = new PluginUtils();
    
    public LoginPlusApp(LoginPlus plus) {
        super(plus.getDescription().getName(), 3);
        this.bukkitLogger = plus.getLogger();
    }
    
    @Override
    protected void onFailed(Reason reason, String message, Throwable throwable) {
        switch (reason) {
        case ALREADY_REGISTERED:
            bukkitLogger.log(Level.SEVERE, "Failed to enable LoginPlusApp; There is a version of LoginPlusApp already installed!");
            break;
        case INCOMPATIBLE:
            bukkitLogger.log(Level.SEVERE, "Another plugin uses a incompatible version of vCompat; LoginPlusApp needs vCompat v3.X");
            break;
        case NO_CONNECTION:
            bukkitLogger.log(Level.SEVERE, "Unable to download a copy of vCompat -> No Connection. LoginPlusApp needs vCompat v3.X");
            bukkitLogger.log(Level.SEVERE, "You can manually donwload vCompat at https://github.com/SourceWriters/vCompat/releases");
            bukkitLogger.log(Level.SEVERE,
                "After downloading it simply put it into plugins/vCompat and be sure that the name is \"vCompat.jar\"");
            break;
        default:
            bukkitLogger.log(Level.SEVERE, "Something went wrong while loading vCompat; Therefore LoginPlusApp cant startup!");
            break;
        }
        bukkitLogger.log(Level.SEVERE, "-----");
        bukkitLogger.log(Level.SEVERE, "Reason: " + reason.name() + " / Message: " + message);
    }
    
    public final PluginUtils getUtils() {
        return utils;
    }
    
    @Override
    protected void onReady() {
        utils.enable();
    }
    
    @Override
    protected void onShutdown() {
        utils.disable();
    }

}
