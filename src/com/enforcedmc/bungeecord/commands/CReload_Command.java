package com.enforcedmc.bungeecord.commands;

import java.io.IOException;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.YamlConfig;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CReload_Command extends Command {
	
	public CReload_Command() {
		super("creload", "enf.admin", "configreload", "reloadconfig");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		try {
			BungeeSuite.getInstance().config = new YamlConfig("config.yml", BungeeSuite.getInstance()).loadConfig();
			sender.sendMessage(new TextComponent("§aYou success reloaded the enfBungee config.yml"));
		} catch(IOException e) {
			e.printStackTrace();
			sender.sendMessage(new TextComponent("§cThere was a problem reloading enfBungee config.yml file"));
		}
	}
}