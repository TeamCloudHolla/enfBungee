package com.enforcedmc.bungeecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlConfig {
	private File configFile;
	private String fileName;
	private Plugin plugin;
	private File folder;
	private Configuration config;
	
	public YamlConfig(final String fileName, final Plugin plugin) {
		this.plugin = plugin;
		this.fileName = fileName;
		configFile = new File(folder = plugin.getDataFolder(), fileName);
	}
	
	public void saveDefaultConfig() {
		if(!folder.exists())
			folder.mkdirs();
		try {
			if(!configFile.exists())
				Files.copy(plugin.getResourceAsStream(fileName), configFile.toPath());
			loadConfig();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Configuration loadConfig() throws IOException {
		return config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
	}
	
	public void saveConfig() throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
	}
	
	public Configuration getConfig() {
		return config;
	}
}