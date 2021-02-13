package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;
import com.syntaxphoenix.loginplus.utils.login.Status;

public class DamageListener implements Listener {
	
	private PluginUtils pluginUtils;
	
	public DamageListener(PluginUtils pluginUtils) {
		this.pluginUtils = pluginUtils;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Status status = pluginUtils.getUserHandler().getStatus((Player) event.getEntity());
			if (status == Status.LOGIN || status == Status.REGISTER || status == Status.CAPTCHA || status == Status.LOGGEDIN) {
				event.setCancelled(true);
			}
		}
	}

}
