package com.syntaxphoenix.loginplus.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.syntaxphoenix.loginplus.utils.PluginUtils;

public class MoveListener implements Listener {
	
	@EventHandler
	public void on(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(PluginUtils.login.contains(p) || PluginUtils.register.contains(p) || PluginUtils.captcha.contains(p)) {
			e.setTo(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getTo().getY(), e.getFrom().getZ(), (float) e.getTo().getYaw(), (float) e.getTo().getPitch()));
		}
	}
}
