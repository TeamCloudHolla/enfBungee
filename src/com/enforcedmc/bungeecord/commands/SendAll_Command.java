package com.enforcedmc.bungeecord.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.commands.handler.CommandTag;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

@CommandTag(usage = "§c§lUSAGE: §7/sendall <from> <to>")
public class SendAll_Command extends Command {
	
	public SendAll_Command() {
		super("sendall", "enf.admin", "gsendall");
	}
	
	@Override
	public void execute(CommandSender cs, String[] args) {
		if(args.length == 2) {
			List<ServerInfo> from = new ArrayList<ServerInfo>();
			ServerInfo to = BungeeCord.getInstance().getServerInfo(args[1]);
			if(BungeeCord.getInstance().getServerInfo(args[0]) != null)
				from.add(BungeeCord.getInstance().getServerInfo(args[0]));
			else if(args[0].equalsIgnoreCase("facs"))
				from.addAll(Arrays.asList(BungeeCord.getInstance().getServerInfo("Factions"), BungeeCord.getInstance().getServerInfo("LibertyFactions")));
			if(from.isEmpty()) {
				cs.sendMessage(new TextComponent(
						"§e§l*§6§l* §e" + args[0] + " §bis not a valid server. Use §e/server §bto list servers or use §e'facs' §e§l*§6§l*"));
				return;
			}
			if(to == null && !(args[1].equalsIgnoreCase("hub") || args[1].equalsIgnoreCase("hubs"))) {
				cs.sendMessage(new TextComponent(
						"§e§l*§6§l* §e" + args[0] + " §bis not a valid server. Use §e/server §bto list servers or use §e'hubs' §e§l*§6§l*"));
				return;
			}
			for(ServerInfo server : from)
				for(ProxiedPlayer player : server.getPlayers())
					player.connect(
							to == null ? BungeeSuite.getInstance().getBalanceLoader().getLowestHub() : to);
		} else
			cs.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
	}
}