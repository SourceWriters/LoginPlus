package com.syntaxphoenix.loginplus;

import org.bukkit.plugin.java.JavaPlugin;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class LoginPlus extends JavaPlugin {

    private LoginPlusApp app;

    @Override
    public void onEnable() {
        if (app != null) {
            return;
        }
        (app = new LoginPlusApp(this)).start();
    }

    @Override
    public void onDisable() {
        if (app == null) {
            return;
        }
        app.stop();
        app = null;
    }

    public final PluginUtils getPluginUtils() {
        return app.getUtils();
    }

    public static LoginPlus getInstance() {
        return LoginPlus.getPlugin(LoginPlus.class);
    }
}
