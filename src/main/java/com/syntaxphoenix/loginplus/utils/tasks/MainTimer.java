package com.syntaxphoenix.loginplus.utils.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;

public class MainTimer extends BukkitRunnable {
	
	private List<Player> kick;
	private List<Player> timerRemove;
	private HashMap<Player, Integer> timer;
	private HashMap<String, Integer> bannedIps;
	
	private MainConfig config;
	
	public MainTimer(MainConfig config) {
		super();
		this.kick = new ArrayList<Player>();
		this.bannedIps = new HashMap<String, Integer>();
		this.timerRemove = new ArrayList<Player>();
		this.timer = new HashMap<Player, Integer>();
		
		this.config = config;
	}

	@Override
	public void run() {	
		for (Player all : timer.keySet()) {
			timer.put(all, timer.get(all)+1);
			if(timer.get(all) > config.getTimerTime()) {
				timerRemove.add(all);
			}
		}
		for (Player all : kick) {
			if (config.isLoginFailedCommandsEnabled()) {
				for (String command : config.getLoginFailedCommands()) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%Player%", all.getName()));
				}
			}
			if (config.isLoginFailedKick()) {
				all.kickPlayer(MessagesConfig.prefix + MessagesConfig.ban_inform.replace("%Time%", config.getLoginFailedBanTime() + ""));
			}
		}
		kick.clear();
		for (String banned : bannedIps.keySet()) { // TODO: Move this to a long-value and timestamp. Saves performance
			bannedIps.put(banned, bannedIps.get(banned) - 1);
			if (bannedIps.get(banned) <= 0) {
				bannedIps.remove(banned);
			}
		}
		for (Player all : timerRemove) {
			all.kickPlayer(MessagesConfig.prefix + MessagesConfig.timer_over.replace("%Reconnect-Time%", config.getReconnectTime() + ""));
			String ip = all.getAddress().getAddress().toString().replace("/", "");
			if (config.isReconnectTimeEnabled()) {
				bannedIps.put(ip, config.getReconnectTime());
			}
			timer.remove(all);
		}
		timerRemove.clear();
	}
	
	public void addTimerPlayer(Player player) {
		this.timer.put(player, 0);
	}
	
	public void removeTimerPlayer(Player player) {
		this.timer.remove(player);
	}
	
	public void addKickPlayer(Player player) {
		this.kick.add(player);
	}
	
	public void addBannedIp(String ip) {
		this.bannedIps.put(ip, config.getLoginFailedBanTime());
	}
	
	public boolean isBannedIp(String ip) {
		return this.bannedIps.containsKey(ip);
	}
	
	public int getRemainingbanTime(String ip) {
		return this.bannedIps.containsKey(ip) ? this.bannedIps.get(ip) : 0;
	}

}
