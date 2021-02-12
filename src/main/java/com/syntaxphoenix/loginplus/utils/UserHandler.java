package com.syntaxphoenix.loginplus.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.syntaxphoenix.loginplus.utils.login.Status;

public class UserHandler {
	
	private HashMap<Player, Status> status;
	private List<Player> awaitingCallback;
	
	public UserHandler() {
		this.status = new HashMap<Player, Status>();
		this.awaitingCallback = new ArrayList<Player>();
	}
	
	public boolean hasStatus(Player player) {
		return this.status.containsKey(player);
	}
	
	public Status getStatus(Player player) {
		return this.status.get(player);
	}
	
	public void removeStatus(Player player) {
		this.status.remove(player);
	}
	
	public void setStatus(Player player, Status status) {
		this.status.put(player, status);
	}
	
	public HashMap<Player, Status> getStatusList() {
		return this.status;
	}
	
	public void addAwaitingCallback(Player player) {
		this.awaitingCallback.add(player);
	}
	
	public boolean isAwaitingCallback(Player player) {
		return this.awaitingCallback.contains(player);
	}
	
	public void removeAwaitingCallback(Player player) {
		this.awaitingCallback.remove(player);
	}
}
