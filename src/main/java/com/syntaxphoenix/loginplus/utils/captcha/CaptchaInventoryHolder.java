package com.syntaxphoenix.loginplus.utils.captcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.utils.ItemUtils;

public class CaptchaInventoryHolder implements InventoryHolder {
	
	private int captchaItems;
	private Inventory inventory;
	private Player player;
	
	public CaptchaInventoryHolder(Player player) {
		this.player = player;
		Random random = new Random();
		this.captchaItems = random.nextInt(5) + 4;
		
		this.inventory = Bukkit.createInventory(this, 27, MessagesConfig.captcha_name);
		
		for (int counter = 0; counter < 27; counter++) {
			inventory.setItem(counter, ItemUtils.DyeCreator(MessagesConfig.captcha_dont_click, null, null, 1, DyeColor.GRAY));
		}
		List<Integer> slots = new ArrayList<Integer>();
		
		for (int count = 0; count < captchaItems; count++) {
			int slot = random.nextInt(27);
			while (slots.contains(slot)) {
				slot = random.nextInt(27);
			}
			slots.add(slot);
			inventory.setItem(slot, ItemUtils.DyeCreator(MessagesConfig.captcha_change, null, null, 1, DyeColor.RED));
		}
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public int getCaptchaItems() {
		return this.captchaItems;
	}
	
	public void removeCaptchaItem() {
		this.captchaItems--;
	}
	
	public Player getPlayer() {
		return this.player;
	}

}
