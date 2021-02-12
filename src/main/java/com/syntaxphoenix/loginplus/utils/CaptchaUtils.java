package com.syntaxphoenix.loginplus.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.syntaxphoenix.loginplus.config.MessagesConfig;

public class CaptchaUtils {
	
	public static HashMap<Player, Integer> captchaParts = new HashMap<Player, Integer>();
	
	public static Inventory createCaptchaInventory(Player player) {
		Random random = new Random();
		int captchaItems = random.nextInt(5) + 4;
		captchaParts.put(player, captchaItems);
		
		Inventory inventory = Bukkit.createInventory(null, 27, MessagesConfig.captcha_name);
		
		for (int counter = 0; counter < 27; counter++) {
			inventory.setItem(counter, ItemUtils.DyeCreator(MessagesConfig.captcha_dont_click, null, null, 1, DyeColor.GRAY));
		}
		List<Integer> slots = new ArrayList<Integer>();
		
		while (captchaItems > 0) {
			captchaItems--;
			int slot = random.nextInt(27);
			while (slots.contains(slot)) {
				slot = random.nextInt(27);
			}
			slots.add(slot);
			inventory.setItem(slot, ItemUtils.DyeCreator(MessagesConfig.captcha_change, null, null, 1, DyeColor.RED));
		}
		
		return inventory;
	}
	
	public static void openInventory(Player player) {
		Inventory inventory = CaptchaUtils.createCaptchaInventory(player); 
		player.openInventory(inventory);
	}
}
