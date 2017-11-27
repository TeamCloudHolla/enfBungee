package com.enforcedmc.bungeecord.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.objects.players.Player;
import com.enforcedmc.bungeecord.objects.players.PlayerRegistry;
import com.enforcedmc.bungeecord.utils.UpdateUtils;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
	private BungeeSuite plugin;
	public HashMap<String, Integer> trys = new HashMap<String, Integer>();
	public long lastuse = System.currentTimeMillis();
	public boolean used = true;

	public PlayerListener() {
		plugin = BungeeSuite.getInstance();
	}

	@EventHandler
	public void onLogin(final PostLoginEvent event) {
		final ProxiedPlayer p = event.getPlayer();
		if(!PlayerRegistry.isPlayer(p.getUniqueId()))
			PlayerRegistry.register(new Player(p.getUniqueId()));
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		final Date current = new Date();
		if(!sdf.format(new Date(BungeeSuite.getInstance().start_time)).equals(sdf.format(current))) {
			BungeeSuite.getInstance().unique_logins = 0;
			BungeeSuite.getInstance().logins = 0;
			BungeeSuite.getInstance().getConfig().set("LastDate", sdf.format(current));
		}
		if(BungeeSuite.getInstance().count < BungeeCord.getInstance().getPlayers().size()) {
			BungeeSuite.getInstance().count = BungeeCord.getInstance().getPlayers().size();
			BungeeCord.getInstance().broadcast(
					new TextComponent("§e(!) §7We are hitting a player record of §6§l#" + BungeeCord.getInstance().getPlayers().size() + "§7!"));
			BungeeSuite.getInstance().getConfig().set("Count", BungeeSuite.getInstance().count);
			BungeeSuite.getInstance().saveConfig();
		}
		if(BungeeSuite.getInstance().getLogins().getString("Players." + p.getUniqueId().toString() + ".LastLogin") != null
				&& !BungeeSuite.getInstance().getLogins().getString("Players." + p.getUniqueId().toString() + ".LastLogin").isEmpty())
			try {
				final Date pdate = sdf.parse(BungeeSuite.getInstance().getLogins().getString("Players." + p.getUniqueId().toString() + ".LastLogin"));
				if(!sdf.format(pdate).equals(sdf.format(current))) {
					BungeeSuite.getInstance().total_count++;
					BungeeSuite.getInstance().unique_logins++;
					BungeeSuite.getInstance().getLogins().set("Players." + p.getUniqueId().toString() + ".LastLogin", sdf.format(current));
					BungeeSuite.getInstance().saveLogins();
				}
				BungeeSuite.getInstance().logins++;
			} catch(Exception ex) {
				BungeeSuite.getInstance().getLogins().set("Players." + p.getUniqueId().toString() + ".LastLogin", sdf.format(current));
				BungeeSuite.getInstance().total_count++;
				BungeeSuite.getInstance().unique_logins++;
				BungeeSuite.getInstance().logins++;
				BungeeSuite.getInstance().saveLogins();
			}
		else {
			BungeeSuite.getInstance().getLogins().set("Players." + p.getUniqueId().toString() + ".LastLogin", sdf.format(current));
			BungeeSuite.getInstance().total_count++;
			BungeeSuite.getInstance().unique_logins++;
			BungeeSuite.getInstance().logins++;
			BungeeSuite.getInstance().saveLogins();
		}
		BungeeSuite.getInstance().getLogins().set("TotalCount", BungeeSuite.getInstance().total_count);
		BungeeSuite.getInstance().getLogins().set("UniqueLogins", BungeeSuite.getInstance().unique_logins);
		BungeeSuite.getInstance().getLogins().set("Logins", BungeeSuite.getInstance().logins);
		BungeeSuite.getInstance().saveConfig();
		if(p.hasPermission("enf.admin"))
			BungeeCord.getInstance().getScheduler().schedule(BungeeSuite.getInstance(), () -> {
				try {
					p.sendMessage(new TextComponent(
							"§8§l«»\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac«»"));
					p.sendMessage(new TextComponent("§6STATISTICS: §8§ §e" + sdf.format(current)));
					p.sendMessage(new TextComponent(" "));
					p.sendMessage(new TextComponent("§a§lTODAY: §6Unique Logins: §e" + BungeeSuite.getInstance().unique_logins));
					p.sendMessage(new TextComponent("§a§lTODAY: §6Logins: §e" + BungeeSuite.getInstance().logins));
					p.sendMessage(new TextComponent(" "));
					p.sendMessage(new TextComponent("§a§lTOTAL: §6Unique Logins: §e" + BungeeSuite.getInstance().total_count));
					p.sendMessage(new TextComponent(" "));
					p.sendMessage(new TextComponent(
							"§8§l«»\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac«»"));
				} catch(Exception ex) {}
			}, 1L, TimeUnit.SECONDS);
		UpdateUtils.check(p);
	}

	public static HashMap<String, Integer> getDomains() {
		final HashMap<String, Integer> count = new HashMap<String, Integer>();
		for(final ProxiedPlayer pl : BungeeCord.getInstance().getPlayers()) {
			final String s = pl.getPendingConnection().getVirtualHost().getHostName().toString().toLowerCase();
			if(count.containsKey(s))
				count.put(s, count.get(s) + 1);
			else
				count.put(s, 1);
		}
		return count;
	}

	@EventHandler
	public void onServerJoin(ServerSwitchEvent event) {
		ProxiedPlayer pl = event.getPlayer();
		if(pl.hasPermission("enf.staff"))
			Utils.broadcast("§e§l(!) §6" + pl.getName() + "§7 has joined §6" + pl.getServer().getInfo().getName() + "§7.", "enf.staff");
	}

	@EventHandler
	public void onNetworkLeave(PlayerDisconnectEvent e) {
		ProxiedPlayer player = e.getPlayer();
		if(player.hasPermission("enf.staff"))
			Utils.broadcast("§e§l(!) §6" + player.getName() + "§7 has disconnected from the network!", "enf.staff");
	}

	@EventHandler
	public void onChat(final ChatEvent event) {
		final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();
		final Player player = PlayerRegistry.getByProxyPlayer(proxiedPlayer);
		if(player.isStaff())
			return;
		String message = event.getMessage();
		for(final ProxiedPlayer pl : BungeeCord.getInstance().getPlayers())
			message = message.replaceAll(pl.getName(), pl.getName().toLowerCase());
		final String caps = message.replaceAll("[0-9 ]", "");
		int found = 0;
		for(char c : caps.toCharArray())
			if(Character.isUpperCase(c))
				++found;
		final int percentage = 30;
		if(percentage > 0 && found >= percentage) {
			event.setMessage(event.getMessage().toLowerCase());
			final String uuid = player.getUUID().toString();
			if(trys.containsKey(uuid))
				trys.put(uuid, trys.get(uuid) + 1);
			else
				trys.put(uuid, 1);
			if(trys.get(uuid) >= 3) {
				Utils.broadcast("§8§l[§c§lSTAFF§8§l] §4" + proxiedPlayer + " §c has been kicked for: 3/3 warnings for excessive §4caps.", "enf.staff");
				proxiedPlayer.disconnect(new TextComponent("§c3/3 warnings for excessive §4caps."));
				trys.remove(uuid);
			} else {
				proxiedPlayer.sendMessage(new TextComponent(
						"§4§l«»\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac«»"));
				proxiedPlayer.sendMessage(new TextComponent("§c§lPlease stop using too many capital letters §a§l(" + found + ")"));
				proxiedPlayer.sendMessage(new TextComponent("§6You have §c" + (3 - trys.get(uuid)) + " §6warnings left before you get kicked!"));
				proxiedPlayer.sendMessage(new TextComponent(
						"§4§l«»\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac«»"));
			}
		}
	}
	
	@EventHandler
	public void onLogin(final LoginEvent e) {
		if(!plugin.getOptions().getBoolean("enabled") || e.isCancelled())
			return;
		final List<String> whitelist = plugin.getOptions().getStringList("whitelist");
		String kickMessage = Utils.toColor(plugin.getOptions().getString("kick_message"));
		if(!whitelist.contains(e.getConnection().getName())) {
			e.setCancelled(true);
			e.setCancelReason(kickMessage);
		}
	}
	
	@EventHandler
	public void onTab(final TabCompleteEvent event) {
		if(!event.getSuggestions().isEmpty())
			return;
		final String[] args = event.getCursor().split(" ");
		final String typed = (args.length > 0 ? args[args.length - 1] : event.getCursor()).toLowerCase();
		if(typed.isEmpty())
			for(final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
				event.getSuggestions().add(player.getName());
		else
			for(final ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
				if(player.getName().toLowerCase().startsWith(typed))
					event.getSuggestions().add(player.getName());
	}
}