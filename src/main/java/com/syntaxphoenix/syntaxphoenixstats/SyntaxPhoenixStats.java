package com.syntaxphoenix.syntaxphoenixstats;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.spigotmc.SpigotConfig;

public class SyntaxPhoenixStats {

	public static final double SYNTAXPHOENIX_STATS_VERSION = 0.4;

	private static final String URL = "https://stats.syntaxphoenix.com/submit/?type=bukkit";

	private File f = new File("plugins/SyntaxPhoenixStats", "config.yml");
	private FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

	private final JavaPlugin plugin;
	private String service_id;

	private static String serverUUID;
	private boolean logging = false;

	public SyntaxPhoenixStats(String service_id, JavaPlugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin can not be null!");
		}
		this.plugin = plugin;
		this.service_id = service_id;
		cfg.addDefault("enabled", true);
		cfg.addDefault("ServerUUID", UUID.randomUUID().toString());
		cfg.addDefault("Logging", false);
		cfg
			.options()
			.header("SyntaxPhoenixStats collects some data for SyntaxPhoenix to give you an better experience. \n"
				+ "This software has no effect on the server performance!\n"
				+ "For more Infos check out https://stats.syntaxphoenix.com")
			.copyDefaults(true);
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		serverUUID = cfg.getString("ServerUUID");
		logging = cfg.getBoolean("Logging");

		if (logging) {
			plugin.getLogger().log(Level.WARNING, "Start Stats-Logger for " + plugin.getName());
		}

		if (cfg.getBoolean("enabled") == true) {
			Bukkit.getServicesManager().register(SyntaxPhoenixStats.class, this, plugin, ServicePriority.Normal);
			DataSubmitter();
			if (logging) {
				plugin.getLogger().log(Level.WARNING, "Starting Submitter for " + plugin.getName());
			}
		}
	}

	private void DataSubmitter() {
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!plugin.isEnabled()) {
					timer.cancel();
					return;
				}

				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						if (logging) {
							plugin.getLogger().log(Level.WARNING, "Submit Data for " + plugin.getName());
						}
						submitData();
					}
				});
			}
		}, 1000 * 60 * 5, 1000 * 60 * 15);
	}

	@SuppressWarnings("unchecked")
	public JSONObject collectPluginData() {
		JSONObject data = new JSONObject();

		String name = plugin.getDescription().getName();
		String version = plugin.getDescription().getVersion();

		data.put("Name", name);
		data.put("Service-ID", this.service_id);
		data.put("Version", version);

		return data;
	}

	public boolean isConnectedToBungee() {
		boolean bungee = SpigotConfig.bungee;
		boolean onlineMode = Bukkit.getServer().getOnlineMode();
		if (bungee && (!(onlineMode))) {
			return true;
		}
		return false;
	}

	public String getServerSoftware() {
		String server_software = "Unknown";
		String version = plugin.getServer().getVersion();
		if (version.contains("Bukkit")) {
			server_software = "Craftbukkit";
		} else if (version.contains("Spigot")) {
			server_software = "Spigot";
		} else if (version.contains("PaperSpigot")) {
			server_software = "PaperSpigot";
		} else {
			server_software = version;
		}
		return server_software;
	}

	@SuppressWarnings("unchecked")
	public JSONObject collectServerData() {
		JSONObject data = new JSONObject();

		int playerAmount = Bukkit.getOnlinePlayers().size();
		boolean onlineMode = Bukkit.getOnlineMode();
		boolean bungeecord = isConnectedToBungee();
		String bukkitVersion = Bukkit.getVersion();
		String server_software = getServerSoftware();
		bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("MC: ") + 4, bukkitVersion.length() - 1);

		String javaVersion = System.getProperty("java.version");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		int coreCount = Runtime.getRuntime().availableProcessors();
		long memory = Runtime.getRuntime().totalMemory();

		data.put("ServerUUID", SyntaxPhoenixStats.serverUUID);

		data.put("BukkitVersion", bukkitVersion);
		data.put("PlayerAmount", playerAmount);
		data.put("onlineMode", onlineMode);
		data.put("BungeeCord", bungeecord);
		data.put("ServerSoftware", server_software);

		data.put("JavaVersion", javaVersion);
		data.put("OS", osName);
		data.put("OS-Version", osVersion);
		data.put("OS-Arch", osArch);
		data.put("Core-Count", coreCount);
		data.put("Memory", memory);

		return data;
	}

	@SuppressWarnings("unchecked")
	private void submitData() {
		JSONObject data = collectServerData();

		JSONArray pluginData = new JSONArray();

		if (logging) {
			plugin.getLogger().log(Level.INFO, "Logging data for " + plugin.getName());
		}
		pluginData.add(collectPluginData());
		data.put("plugins", pluginData);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendDatatoServer(data);
				} catch (Exception e) {
					if (logging) {
						plugin
							.getLogger()
							.log(Level.WARNING, "Could not submit plugin stats of " + plugin.getName(), e);
					}
				}
			}
		}).start();
	}

	private void sendDatatoServer(JSONObject data) throws Exception {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null!");
		}
		if (Bukkit.isPrimaryThread()) {
			throw new IllegalAccessException("This method must not be called from the main thread!");
		}

		HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();

		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "SyntaxPhoenixStats-ServerVersion/" + SYNTAXPHOENIX_STATS_VERSION);

		connection.setDoOutput(true);
		PrintStream ps = new PrintStream(connection.getOutputStream());
		ps.print("data=" + data.toJSONString());
		connection.getInputStream();

		ps.close();
		connection.getInputStream().close();
	}
}
