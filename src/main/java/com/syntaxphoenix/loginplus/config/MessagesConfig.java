package com.syntaxphoenix.loginplus.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesConfig {
	
	public static File f = new File("plugins/LoginPlus", "messages.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public static String prefix = "&8[&6Login&ePlus&8] &7";
	public static String already_logged_in = "&cAnother User with the same Account is already logged in";
	public static String banned = "&7Youre currently &cbanned &7for Login, please try again in &c%BanTime% &7Seconds.";
	public static String timer_over = "&7The Timer is up! You can reconnect and try again in &c%Reconnect-Time% &7Seconds!";
	public static String timer_inform = "&7You have &c%Time% &7Seconds to log into your Account";
	public static String ban_inform = "&7You have been banned for &c%Time% &7Seconds";
	public static String captcha_failed = "&cCaptcha failed";
	public static String no_permission = "&cYou're not permitted to use this command";
	public static String wrong_arguments = "&cWrong Arguments";
	public static String player_offline = "&cThis player is offline!";
	
	public static String title_login_title = "&cPlease log in";
	public static String title_login_subtitle = "&6Enter your password into the Chat";
	public static String title_register_title = "&cPlease register";
	public static String title_register_subtitle = "&6Enter your password into the Chat";
	public static String title_register_success_title = "&aSuccessfully registered";
	public static String title_register_success_subtitle = "&6Your Account has successfully been registered";
	public static String title_login_success_title = "&aSuccessfully logged in";
	public static String title_login_success_subtitle = "&7You can play now";
	public static String title_login_failed_title = "&cIncorrect Password";
	public static String title_login_failed_subtitle = "&7You have &c%Attempts% &7Attempts left";
	public static String title_changepw_title = "&cChange password";
	public static String title_changepw_subtitle = "&6Enter your password into the Chat";
	public static String title_changepw_success_title = "&aPassword has been changed";
	public static String title_changepw_success_subtitle = "&6Your Password has successfully been changed";
	
	public static String premium_disabled = "&6Premium&7-Function has been &cdisabled";
	public static String premium_enabled = "&6Premium&7-Function has been &aenabled";
	public static String no_premium = "&7Your Account is not an Premium-Account!";
	
	public static String captcha_name = "&4Captcha";
	public static String captcha_dont_click = "&7Dont click";
	public static String captcha_change = "&4Change";
	public static String captcha_changed = "&aChanged";
	
	public static void load() {
		prefix = fixColors(setObject("global.prefix", prefix));
		already_logged_in = fixColors(setObject("error.already_loggedin", already_logged_in));
		banned = fixColors(setObject("error.banned", banned));
		timer_over = fixColors(setObject("error.timer_over", timer_over));
		captcha_failed = fixColors(setObject("error.captcha_failed", captcha_failed));
		timer_inform = fixColors(setObject("timer.info", timer_inform));
		ban_inform = fixColors(setObject("ban.info", ban_inform));
		no_permission = fixColors(setObject("error.permission", no_permission));
		wrong_arguments = fixColors(setObject("error.arguments", wrong_arguments));
		player_offline = fixColors(setObject("error.player_offline", player_offline));
		
		title_login_title = fixColors(setObject("title.login.title", title_login_title));
		title_login_subtitle = fixColors(setObject("title.login.subtitle", title_login_subtitle));
		title_register_title = fixColors(setObject("title.register.title", title_register_title));
		title_register_subtitle = fixColors(setObject("title.register.subtitle", title_register_subtitle));
		title_register_success_title = fixColors(setObject("title.register_success.title", title_register_success_title));
		title_register_success_subtitle = fixColors(setObject("title.register_success.subtitle", title_register_success_subtitle));
		title_login_success_title = fixColors(setObject("title.login_success.title", title_login_success_title));
		title_login_success_subtitle = fixColors(setObject("title.login_success.subtitle", title_login_success_subtitle));
		title_login_failed_title = fixColors(setObject("title.login_failed.title", title_login_failed_title));
		title_login_failed_subtitle = fixColors(setObject("title.login_failed.subtitle", title_login_failed_subtitle));
		title_changepw_title = fixColors(setObject("title.change_password.title", title_changepw_title));
		title_changepw_subtitle = fixColors(setObject("title.change_password.subtitle", title_changepw_subtitle));
		title_changepw_success_title = fixColors(setObject("title.change_password_success.title", title_changepw_success_title));
		title_changepw_success_subtitle = fixColors(setObject("title.change_password_success.subtitle", title_changepw_success_subtitle));
		
		captcha_name = fixColors(setObject("captcha.inventory.name", captcha_name));
		captcha_dont_click = fixColors(setObject("captcha.inventory.dont_click", captcha_dont_click));
		captcha_change = fixColors(setObject("captcha.inventory.change", captcha_change));
		captcha_changed = fixColors(setObject("captcha.inventory.changed", captcha_changed));
		
		premium_disabled = fixColors(setObject("premium.disabled", premium_disabled)); 
		premium_enabled = fixColors(setObject("premium.enabled", premium_enabled)); 
		no_premium = fixColors(setObject("premium.no", no_premium));
	}
	
	public static String fixColors(String code) {
		code = code.replace("&", "§");
		return code;
	}
	
	public static String setObject(String path, String obj) {
		if(cfg.contains(path)) {
			return cfg.getString(path);
		} else {
			cfg.set(path, obj);
			save();
			return obj;
		}
	}
	
	public static void save() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
