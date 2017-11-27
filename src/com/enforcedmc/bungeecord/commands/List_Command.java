package com.enforcedmc.bungeecord.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class List_Command extends Command {
	
	public List_Command() {
		super("list");
	}

	@Override
	public void execute(CommandSender s, String[] args) {
		List<ProxiedPlayer> enf = new ArrayList<ProxiedPlayer>();
		List<ProxiedPlayer> lib = new ArrayList<ProxiedPlayer>();
		List<ProxiedPlayer> other = new ArrayList<ProxiedPlayer>();
		for(ProxiedPlayer pl : BungeeCord.getInstance().getPlayers())
			if(pl.hasPermission("enf.staff"))
				if(pl.getServer().getInfo().getName().equals("Factions"))
					enf.add(pl);
				else if(pl.getServer().getInfo().getName().equals("LibertyFactions"))
					lib.add(pl);
				else
					other.add(pl);
		if(!enf.isEmpty() || !lib.isEmpty() || !other.isEmpty()) {
			s.sendMessage(new TextComponent(" "));
			s.sendMessage(new TextComponent(" "));
		}
		if(!enf.isEmpty())
			s.sendMessage(new TextComponent("            §e§lEnforced Factions"));
		for(ProxiedPlayer pl : enf)
			s.sendMessage(new TextComponent("§e§l* " + Utils.toColor(getGroup(pl)) + "§e" + pl.getName()));
		if(!enf.isEmpty() && !lib.isEmpty())
			s.sendMessage(new TextComponent(" "));
		if(!lib.isEmpty())
			s.sendMessage(new TextComponent("            §e§lLiberty Factions"));
		for(ProxiedPlayer pl : lib)
			s.sendMessage(new TextComponent("§e§l* " + Utils.toColor(getGroup(pl)) + "§e" + pl.getName()));
		if((!enf.isEmpty() || !lib.isEmpty()) && !other.isEmpty())
			s.sendMessage(new TextComponent(" "));
		for(ProxiedPlayer pl : other)
			s.sendMessage(new TextComponent(
					"§e§l* " + Utils.toColor(getGroup(pl)) + "§e" + pl.getName() + "§7 is connected to §6" + pl.getServer().getInfo().getName() + "§7."));
		s.sendMessage(new TextComponent(" "));
		s.sendMessage(new TextComponent("§7There is §e" + BungeeCord.getInstance().getServerInfo("Factions").getPlayers().size()
				+ " §7players online §eEnforced Factions§7!\n" + "§7There is §e"
				+ BungeeCord.getInstance().getServerInfo("LibertyFactions").getPlayers().size() + " §7players online §eLiberty Factions§7!"));
		s.sendMessage(new TextComponent(" "));
		s.sendMessage(new TextComponent("§7There are §e" + BungeeCord.getInstance().getOnlineCount() + "§7 total players online §eEnforced§6MC§7!"));
		s.sendMessage(new TextComponent(" "));
		s.sendMessage(new TextComponent(" "));
	}
	
	private String getGroup(final ProxiedPlayer p) {
		try {
			Map<String, String> ranks = new HashMap<String, String>();
			ranks.put("owner", "&7(&4&lOwner&7) ");
			ranks.put("admin", "&7(&c&lAdmin&7) ");
			ranks.put("srstaff", "&7(&5&lMod&7) ");
			ranks.put("staff", "&7(&2&lHelper&7) ");
			ranks.put("jrmod", "&7(&b&lJrMod&7) ");
			ranks.put("srmod", "&7(&6&lSrMod&7) ");
			ranks.put("helper", "&7(&2&lHelper&7) ");
			ranks.put("helper+", "&7(&2&lHelper+&7) ");
			ranks.put("manager", "&7(&4&lManager&7) ");
			final File file = new File(BungeeSuite.getInstance().getDataFolder().getParentFile().getParent(), "config.yml");
			final Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			if(config.getStringList("groups." + p.getName()) != null) {
				String rank = config.getStringList("groups." + p.getName()).get(0);
				return ranks.get(rank);
				//if(BungeeSuite.getInstance().getCache().getString("staff_chat_prefix." + rank) != null)
				//	return BungeeSuite.getInstance().getCache().getString("staff_chat_prefix." + rank);
			}
		} catch(IOException e) {}
		return "";
	}
}