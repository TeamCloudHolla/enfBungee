package com.enforcedmc.bungeecord.commands;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.enforcedmc.bungeecord.BungeeSuite;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping_Command extends Command {
	
	public Ping_Command() {
		super("ping");
	}

	public HashMap<String, Integer> ping2 = new HashMap<String, Integer>();
	public HashMap<String, Integer> ping3 = new HashMap<String, Integer>();

	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!(cs instanceof ProxiedPlayer))
			return;
		final ProxiedPlayer p = (ProxiedPlayer) cs;
		final String uuid = p.getUniqueId().toString();

		p.sendMessage(new TextComponent("§aGetting your ping, please wait..."));

		final int ping1 = p.getPing();

		BungeeCord.getInstance().getScheduler().schedule(BungeeSuite.getInstance(), () -> {
			if(p.getPendingConnection() != null)
				ping2.put(uuid, p.getPing());
		}, 1L, TimeUnit.SECONDS);

		BungeeCord.getInstance().getScheduler().schedule(BungeeSuite.getInstance(), () -> {
			if(p.getPendingConnection() != null) {
				ping3.put(uuid, p.getPing());

				int average = (ping1 + ping2.get(uuid) + ping3.get(uuid)) / 3;

				p.sendMessage(new TextComponent("§8---------------"));
				p.sendMessage(new TextComponent("§7Try #1: §a" + ping1 + "ms"));
				p.sendMessage(new TextComponent("§7Try #2: §a" + ping2.get(uuid) + "ms"));
				p.sendMessage(new TextComponent("§7Try #3: §a" + ping3.get(uuid) + "ms"));
				p.sendMessage(new TextComponent("§7Average Ping: §a" + average + "ms"));
				p.sendMessage(new TextComponent("§8---------------"));
				ping2.remove(uuid);
				ping3.remove(uuid);
			}
		}, 2L, TimeUnit.SECONDS);
	}
}