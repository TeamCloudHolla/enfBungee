package com.enforcedmc.bungeecord.commands;

import java.util.ArrayList;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.commands.handler.CommandTag;
import com.enforcedmc.bungeecord.objects.players.Player;
import com.enforcedmc.bungeecord.objects.players.PlayerRegistry;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

@CommandTag(usage = "§c§lUSAGE: §7/disableserver <server>")
public class Server_Command extends Command {
	public static ArrayList<String> disabled = new ArrayList<String>();

	public Server_Command() {
		super("disableserver", "enf.admin", "disable");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		ProxiedPlayer pl;
		Player p;
		if(args.length == 1) {
			ServerInfo server = BungeeCord.getInstance().getServerInfo(args[0]);
			if(server != null) {
				if(disabled.contains(server.getName())) {
					disabled.remove(server.getName());

					cs.sendMessage(new TextComponent("§e§lSERVER: §7You enabled the server " + server.getName() + "."));
					Utils.broadcast("§8§l[§c§lSTAFF§8§l] §e" + cs + " §a§lENABLED§6 the server: §e" + server.getName() + "§6.", "enf.staff");
				} else {
					disabled.add(server.getName());
					for(Object element : server.getPlayers()) {
						pl = (ProxiedPlayer) element;

						p = PlayerRegistry.getByProxyPlayer(pl);
						if(!p.isStaff()) {
							pl.connect(BungeeSuite.getInstance().getBalanceLoader().getLowestHub());
							pl.sendMessage(new TextComponent("§c§lSERVER: §7You have been moved to " + pl.getServer().getInfo().getName() + " because "
									+ server.getName() + " has been disabled."));
						}
					}
					cs.sendMessage(new TextComponent("§e§lSERVER: §7You disabled the server " + server.getName() + "."));
					Utils.broadcast("§8§l[§c§lSTAFF§8§l] §e" + cs + " §c§lDISABLED§6 the server: §e" + server.getName() + "§6.", "enf.staff");
				}
			} else
				cs.sendMessage(new TextComponent("§c§lSERVER: §7The server " + args[0] + " does not exists!"));
		} else
			cs.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
	}
}