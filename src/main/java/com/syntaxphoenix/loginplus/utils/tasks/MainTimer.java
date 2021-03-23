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
	private HashMap<String, Long> bannedIps;
	
	private MainConfig config;
	
	public MainTimer(MainConfig config) {
		super();
		this.kick = new ArrayList<Player>();
		this.bannedIps = new HashMap<String, Long>();
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
		for (Player all : timerRemove) {
			all.kickPlayer(MessagesConfig.prefix + MessagesConfig.timer_over.replace("%Reconnect-Time%", config.getReconnectTime() + ""));
			String ip = all.getAddress().getAddress().toString().replace("/", "");
			if (config.isReconnectTimeEnabled()) {
				long timestamp = System.currentTimeMillis();
				bannedIps.put(ip, timestamp + (config.getReconnectTime() * 1000));
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
		long timestamp = System.currentTimeMillis();
		this.bannedIps.put(ip, timestamp + (config.getLoginFailedBanTime() * 1000));
	}
	
	public boolean isBannedIp(String ip) {
		if (this.bannedIps.containsKey(ip)) {
			long oldTime = this.bannedIps.get(ip);
			long timestamp = System.currentTimeMillis();
			if (timestamp <= oldTime) {
				return true;
			}
		}
		return false;
	}
	
	public int getRemainingbanTime(String ip) {
		if (this.bannedIps.containsKey(ip)) {
			long oldTime = this.bannedIps.get(ip);
			long timestamp = System.currentTimeMillis();
			return (int) (timestamp - oldTime);
		}
		return 0;
	}

}
