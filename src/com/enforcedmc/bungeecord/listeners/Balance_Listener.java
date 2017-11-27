package com.enforcedmc.bungeecord.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.commands.Server_Command;
import com.enforcedmc.bungeecord.objects.players.Player;
import com.enforcedmc.bungeecord.objects.players.PlayerRegistry;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Balance_Listener implements Listener {
	private Map<ProxiedPlayer, ServerInfo> moving = new HashMap<ProxiedPlayer, ServerInfo>();
	private List<ProxiedPlayer> nomsg = new ArrayList<ProxiedPlayer>();
	private final BungeeSuite BUNGEE_SUITE;
	
	public Balance_Listener(BungeeSuite BUNGEE_SUITE) {
		this.BUNGEE_SUITE = BUNGEE_SUITE;
	}
	
	@EventHandler
	public void onLogin(ServerConnectEvent event) {
		final ProxiedPlayer player = event.getPlayer();
		final ServerInfo target = event.getTarget();
		if(player.hasPermission("enf.admin"))
			return;
		if(target != null && BungeeSuite.getInstance().getBalanceLoader().isHub(target)) {
			final ServerInfo new_target = BungeeSuite.getInstance().getBalanceLoader().getLowestHub();
			if(new_target == null)
				return;
			event.setTarget(new_target);
		}
	}
	
	@EventHandler
	public void onServerSwitch(ServerConnectEvent event) {
		ProxiedPlayer pl = event.getPlayer();
		ServerInfo target = event.getTarget();
		Player p = PlayerRegistry.getByProxyPlayer(pl);
		if(Server_Command.disabled.contains(target.getName()) && !p.isStaff()) {
			event.setCancelled(true);
			pl.sendMessage(new TextComponent("§c§lERROR: §7The server your trying to connect to is currently disabled."));
		}
			pl.sendMessage(new TextComponent("\n                      §e§lEnforcedMC\n      §bPlease wait while you are transferred\n"));
		}
	}