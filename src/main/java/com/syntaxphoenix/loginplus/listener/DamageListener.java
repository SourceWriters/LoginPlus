package com.syntaxphoenix.loginplus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class DamageListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void on(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (PluginUtils.login.contains(player) || PluginUtils.register.contains(player) || PluginUtils.captcha.contains(player)) {
				event.setCancelled(true);
			}
		}
	}

}
