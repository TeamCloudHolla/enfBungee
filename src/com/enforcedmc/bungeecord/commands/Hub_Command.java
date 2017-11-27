package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.BungeeSuite;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub_Command extends Command {
	
	public Hub_Command() {
		super("hub", null, "lobby");
	}
	
	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!(cs instanceof ProxiedPlayer))
			return;
		ProxiedPlayer p = (ProxiedPlayer) cs;
		if(args.length == 1 && args[0].equalsIgnoreCase("sort")) {
			int amt = 0;
			for(ServerInfo server : BungeeSuite.getInstance().getBalanceLoader().getAllHubs())
				amt += server.getPlayers().size();
			amt /= BungeeSuite.getInstance().getBalanceLoader().getAllHubs().size();
			if(amt == 0)
				amt++;
			for(ServerInfo server : BungeeSuite.getInstance().getBalanceLoader().getAllHubs())
				for(ProxiedPlayer player : server.getPlayers())
					if(amt < server.getPlayers().size())
						for(int i = 1; i < 7; i++)
							if(BungeeCord.getInstance().getServerInfo("Hub" + i).getPlayers().size() <= amt) {
								player.connect(BungeeCord.getInstance().getServerInfo("Hub" + i));
								break;
							}
			return;
		}
		ServerInfo server = p.getServer().getInfo();
		if(server != null && !server.getName().startsWith("Hub")) {
			ServerInfo hub = BungeeSuite.getInstance().getBalanceLoader().getLowestHub();
			p.sendMessage(new TextComponent("§aConnecting you to the best available hub!"));
			p.connect(hub);
		} else
			p.sendMessage(new TextComponent("§c§lERROR: §7You are already connected to a Hub!"));
	}
}