package com.enforcedmc.bungeecord.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.commands.handler.CommandTag;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@CommandTag(usage = "§c§lUSAGE: §7/enfperm <add/remove/list> <group/player> <perm>\n§c§lUSAGE: §7/enfperm ranks\n§c§lUSAGE: §7/enfperm players")
public class Perm_Command extends Command {
	
	public Perm_Command() {
		super("enfperm", "enf.admin", "bperm", "perm");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
			return;
		}
		final File file = new File(BungeeSuite.getInstance().getDataFolder().getParentFile().getParent(), "config.yml");
		Configuration config = null;
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch(IOException e) {
			sender.sendMessage(new TextComponent("§cError obtaining Bungee config.yml file"));
			return;
		}
		if(args.length == 1 && (args[0].equalsIgnoreCase("ranks") || args[0].equalsIgnoreCase("groups"))) {
			final List<String> ranks = new ArrayList<String>();
			for(String rank : config.getSection("permissions").getKeys())
				ranks.add("&7&o" + rank);
			sender.sendMessage(new TextComponent("§eRanks: §7§o" + ranks.toString()));
			return;
		} else if(args.length == 1 && args[0].equalsIgnoreCase("players")) {
			final List<String> players = new ArrayList<String>();
			for(String name : config.getSection("groups").getKeys())
				players.add("&7&o" + name);
			sender.sendMessage(new TextComponent("§ePlayers: §7§o" + players.toString()));
			return;
		} else if(args.length == 1) {
			sender.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
			return;
		}
		final boolean group = config.getStringList("permissions." + args[1]) != null;
		final List<String> perms = group
				? config.getStringList("permissions." + args[1])
				: config.getStringList("groups." + args[1]) != null ? config.getStringList("groups." + args[1]) : new ArrayList<String>();
		if(args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(new TextComponent("§e" + args[1] + " permissions:"));
			for(String s : perms)
				sender.sendMessage(new TextComponent(" §7- §o" + s));
			return;
		} else if(args.length == 2) {
			sender.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
			return;
		}
		if(args[0].equalsIgnoreCase("add"))
			perms.add(args[2]);
		else if(args[0].equalsIgnoreCase("remove"))
			perms.remove(args[2]);
		else {
			sender.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
			return;
		}
		config.set((group ? "permissions." : "groups.") + args[1], perms);
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
		} catch(IOException e) {}
		BungeeCord.getInstance().config.load();
		sender.sendMessage(new TextComponent("§b" + (args[0].equalsIgnoreCase("add") ? "Added" : "Removed") + " §e" + args[2] + " §bto §e" + args[1]));
	}
}