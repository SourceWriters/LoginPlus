package com.syntaxphoenix.loginplus.utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;

import com.syntaxphoenix.loginplus.LoginPlus;
import com.syntaxphoenix.loginplus.accounts.database.AccountDatabase;
import com.syntaxphoenix.loginplus.accounts.database.ConfigAccountDatabase;
import com.syntaxphoenix.loginplus.accounts.database.MysqlAccountDatabase;
import com.syntaxphoenix.loginplus.accounts.manager.AccountManager;
import com.syntaxphoenix.loginplus.commands.ChangePasswordCommand;
import com.syntaxphoenix.loginplus.config.MysqlConfig;
import com.syntaxphoenix.loginplus.config.MainConfig;
import com.syntaxphoenix.loginplus.config.MessagesConfig;
import com.syntaxphoenix.loginplus.encryption.EncryptionManager;
import com.syntaxphoenix.loginplus.listener.BlockListener;
import com.syntaxphoenix.loginplus.listener.ChatListener;
import com.syntaxphoenix.loginplus.listener.CommandListener;
import com.syntaxphoenix.loginplus.listener.DamageListener;
import com.syntaxphoenix.loginplus.listener.InteractListener;
import com.syntaxphoenix.loginplus.listener.InventoryClearListener;
import com.syntaxphoenix.loginplus.listener.InventoryListener;
import com.syntaxphoenix.loginplus.listener.JoinListener;
import com.syntaxphoenix.loginplus.listener.LoginListener;
import com.syntaxphoenix.loginplus.listener.MoveListener;
import com.syntaxphoenix.loginplus.listener.QuitListener;
import com.syntaxphoenix.loginplus.mysql.Mysql;
import com.syntaxphoenix.loginplus.utils.login.LoginManager;
import com.syntaxphoenix.loginplus.utils.login.Status;
import com.syntaxphoenix.loginplus.utils.session.SessionManager;
import com.syntaxphoenix.loginplus.utils.tasks.MainTimer;
import com.syntaxphoenix.syntaxphoenixstats.SyntaxPhoenixStats;

public class PluginUtils {
	
	public static HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>(); //TODO: Move to UserHandler
	public static String version = "";
	
	private AccountManager accountManager;
	private UserHandler userHandler;
	private EncryptionManager encryptionManager;
	private LoginManager loginManager;
	private SessionManager sessionManager;
	
	private Mysql mysql;
	
	private MainConfig config;
	private MysqlConfig mysqlConfig;
	
	private MainTimer timer;
	
	public PluginUtils() {
		//This section is under Copyright, dont remove this Logo!
		System.out.println("   __             _         ___ _           ");
		System.out.println("  / /  ___   __ _(_)_ __   / _ \\ |_   _ ___ ");
		System.out.println(" / /  / _ \\ / _` | | '_ \\ / /_)/ | | | / __|");
		System.out.println("/ /__| (_) | (_| | | | | / ___/| | |_| \\__ \\");
		System.out.println("\\____/\\___/ \\__, |_|_| |_\\/    |_|\\__,_|___/");
		System.out.println("            |___/                           ");
		System.out.println("                                            ");		
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		sender.sendMessage("§eThank you for using LoginPlus by §bSyntax§9Phoenix §7IT-Solutions");
	}
	
	public void enable() {
        loadConfigs();
        loadMysql();
        this.accountManager = new AccountManager(this);
        this.userHandler = new UserHandler();
        this.encryptionManager = new EncryptionManager(config);
        this.loginManager = new LoginManager(this);
        this.sessionManager = new SessionManager(config);
        loadTimer();
        loadListener();
        loadCommands();
        
        new SyntaxPhoenixStats("sw9Z6c1f", LoginPlus.getInstance());
	}
	
	public void disable() {
		for (Player all : PluginUtils.inventories.keySet()) {
			InventoryClearListener.setInventory(all);
		}
		for (Player all : this.userHandler.getStatusList().keySet()) {
			Status status = this.userHandler.getStatusList().get(all);
			if (status == Status.CAPTCHA || status == Status.LOGIN || status == Status.REGISTER) {
				all.kickPlayer("");
			}
		}
		if (mysqlConfig.isEnabled() && this.mysql != null) {
			this.mysql.disconnect();
		}		
	}
	
	public static String getServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		return version;
	}
	
	public MainTimer getTimer() {
		return this.timer;
	}
	
	public AccountManager getAccountManager() {
		return this.accountManager;
	}
	
	public UserHandler getUserHandler() {
		return this.userHandler;
	}
	
	public EncryptionManager getEncryptionManager() {
		return this.encryptionManager;
	}
	
	public LoginManager getLoginManager() {
		return this.loginManager;
	}
	
	public SessionManager getSessionManager() {
		return this.sessionManager;
	}
	
	public MainConfig getConfig() {
		return this.config;
	}
	
	public AccountDatabase createAccountDatabase() {
		if (this.mysqlConfig.isEnabled()) {
			return new MysqlAccountDatabase(this.mysql);
		}
		return new ConfigAccountDatabase();
	}
	
	private void loadTimer() {
		this.timer = new MainTimer(this.config);
		this.timer.runTaskTimer(LoginPlus.getInstance(), 15, 20);
	}
	
	private void loadMysql() {
		if (this.mysqlConfig.isEnabled()) {			
			this.mysql = new Mysql(
				mysqlConfig.getAddress(),
				mysqlConfig.getPort(),
				mysqlConfig.getDatabase(),
				mysqlConfig.getUsername(),
				mysqlConfig.getPassword()
			);
		
			try {
				Statement st = mysql.getConnection().createStatement();
				st.executeUpdate("CREATE TABLE IF NOT EXISTS `accounts`(`username` varchar(16),`hash` varchar(512),`type` varchar(16),`premium` boolean)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadCommands() {
		LoginPlus.getInstance().getCommand("changepw").setExecutor(new ChangePasswordCommand(this));
		//LoginPlus.m.getCommand("premium").setExecutor(new PremiumCommand());
	}
	
	private void loadListener() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinListener(this), LoginPlus.getInstance());
		pm.registerEvents(new LoginListener(this), LoginPlus.getInstance());
		pm.registerEvents(new MoveListener(this), LoginPlus.getInstance());
		pm.registerEvents(new ChatListener(this), LoginPlus.getInstance());
		pm.registerEvents(new BlockListener(this), LoginPlus.getInstance());
		pm.registerEvents(new CommandListener(this), LoginPlus.getInstance());
		pm.registerEvents(new InteractListener(this), LoginPlus.getInstance());
		pm.registerEvents(new InventoryListener(this), LoginPlus.getInstance());
		pm.registerEvents(new InventoryClearListener(this), LoginPlus.getInstance());
		pm.registerEvents(new DamageListener(this), LoginPlus.getInstance());
		pm.registerEvents(new QuitListener(this), LoginPlus.getInstance());
	}
	
	private void loadConfigs() {
		this.config = new MainConfig();
		this.mysqlConfig = new MysqlConfig();
		MessagesConfig.load();
	}
}
