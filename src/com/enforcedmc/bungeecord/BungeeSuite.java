package com.enforcedmc.bungeecord;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.enforcedmc.bungeecord.commands.Alert_Command;
import com.enforcedmc.bungeecord.commands.BungeeVanish;
import com.enforcedmc.bungeecord.commands.CReload_Command;
import com.enforcedmc.bungeecord.commands.HubCount;
import com.enforcedmc.bungeecord.commands.Hub_Command;
import com.enforcedmc.bungeecord.commands.KickAll_Command;
import com.enforcedmc.bungeecord.commands.List_Command;
import com.enforcedmc.bungeecord.commands.Perm_Command;
import com.enforcedmc.bungeecord.commands.Ping_Command;
import com.enforcedmc.bungeecord.commands.Report_Command;
import com.enforcedmc.bungeecord.commands.SPing_Command;
import com.enforcedmc.bungeecord.commands.SendAll_Command;
import com.enforcedmc.bungeecord.commands.Server_Command;
import com.enforcedmc.bungeecord.commands.StaffChat_Command;
import com.enforcedmc.bungeecord.commands.Updates_Command;
import com.enforcedmc.bungeecord.commands.Uptime_Command;
import com.enforcedmc.bungeecord.listeners.Adv_Listener;
import com.enforcedmc.bungeecord.listeners.Balance_Listener;
import com.enforcedmc.bungeecord.listeners.PingListener;
import com.enforcedmc.bungeecord.listeners.PlayerListener;
import com.enforcedmc.bungeecord.objects.balancer.BalanceLoader;
import com.enforcedmc.bungeecord.utils.UpdateUtils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeSuite extends Plugin {
	public Configuration loginsConfig;
	public File loginsFile;
	public YamlConfig options;
	private BalanceLoader BALANCE_LOADER;
	private static BungeeSuite instance;
	public Configuration config;
	public File cfg;
	public String default_server = "Hub1";
	public long start_time = System.currentTimeMillis();
	public int unique_logins = 0;
	public int logins = 0;
	public int count = 0;
	public int total_count = 0;
	public SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
	public YamlConfig updates;

	@Override
	public void onDisable() {
		getConfig().set("UniqueLogins", unique_logins);
		getConfig().set("Logins", logins);
		getConfig().set("TotalCount", total_count);
		saveConfig();
		instance = null;
		dateformat = null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onEnable() {
		instance = this;
		(options = new YamlConfig("options.yml", this)).saveDefaultConfig();
		(updates = new YamlConfig("updates.yml", this)).saveDefaultConfig();
		try {
			options.loadConfig();
			updates.loadConfig();
		} catch(IOException e1) {
			e1.printStackTrace();
		}
		UpdateUtils.load();
		PingListener.load();
		BALANCE_LOADER = new BalanceLoader();
		if(!getDataFolder().exists())
			getDataFolder().mkdir();
		try {
			cfg = new File(getDataFolder() + "/config.yml");
			if(!cfg.exists())
				cfg.createNewFile();
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfg);

			loginsFile = new File(getDataFolder(), "/logins.yml");
			if(!loginsFile.exists())
				loginsFile.createNewFile();
			loginsConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loginsFile);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		for(final ListenerInfo li : BungeeCord.getInstance().getConfigurationAdapter().getListeners())
			default_server = li.getDefaultServer();
		initCommands(new Alert_Command(), new CReload_Command(), new Hub_Command(), new List_Command(), new Perm_Command(), new Ping_Command(),
				new Report_Command(), new SendAll_Command(), new Server_Command(), new SPing_Command(), new StaffChat_Command(), new Updates_Command(),
				new Uptime_Command(), new KickAll_Command(), new HubCount(), new BungeeVanish());
		initListeners(new PlayerListener(), new Balance_Listener(this), new Adv_Listener(), new PingListener());
		start_time = System.currentTimeMillis();
		if(getConfig().getString("LastDate") != null && !getConfig().getString("LastDate").isEmpty()) {
			Date old = new Date();
			try {
				old = dateformat.parse(getConfig().getString("LastDate"));
			} catch(ParseException e3) {
				e3.printStackTrace();
			}
			if(!dateformat.format(old).equals(dateformat.format(new Date()))) {
				unique_logins = 0;
				logins = 0;
				getConfig().set("UniqueLogins", 0);
				getConfig().set("Logins", 0);
				getConfig().set("LastDate", dateformat.format(new Date()));
				saveConfig();
			} else {
				unique_logins = getConfig().getInt("UniqueLogins");
				logins = getConfig().getInt("Logins");
			}
		} else {
			getConfig().set("LastDate", dateformat.format(new Date()));
			saveConfig();
		}
		try {
			total_count = getConfig().getInt("TotalCount");
			count = getConfig().getInt("Count");
		} catch(Exception ex) {
			getConfig().set("TotalCount", total_count);
			saveConfig();
		}
		try {
			count = getConfig().getInt("Count");
		} catch(Exception ex) {
			getConfig().set("Count", count);
			saveConfig();
		}
		/*
		owner: "&7(&4&lOwner&7) "
		admin: "&7(&c&lAdmin&7) "
		srstaff: "&7(&5&lMod&7) "
		staff: "&7(&2&lHelper&7) "
		jrmod: "&7(&b&lJrMod&7) "
		srmod: "&7(&6&lSrMod&7) "
		helper: "&7(&2&lHelper&7) "
		helper+: "&7(&2&lHelper+&7) "
		manager: "&7(&4&lManager&7) "
		 */
	}

	public Configuration getOptions() {
		return options.getConfig();
	}

	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, cfg);
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfg);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void saveLogins() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(loginsConfig, loginsFile);
			loginsConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loginsFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Configuration getConfig() {
		return config;
	}
	
	public Configuration getLogins() {
		return loginsConfig;
	}

	public static BungeeSuite getInstance() {
		return instance;
	}

	private final void initCommands(final Command... commands) {
		for(final Command command : commands)
			getProxy().getPluginManager().registerCommand(this, command);
	}

	private final void initListeners(final Listener... listeners) {
		for(final Listener listener : listeners)
			getProxy().getPluginManager().registerListener(this, listener);
	}

	public final BalanceLoader getBalanceLoader() {
		return BALANCE_LOADER;
	}
}