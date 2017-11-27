package com.enforcedmc.bungeecord.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.enforcedmc.bungeecord.BungeeSuite;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class UpdateUtils {
	public static HashMap<Integer, List<String>> updates = new HashMap<Integer, List<String>>();
	private static HashMap<UUID, Integer> players = new HashMap<UUID, Integer>();
	public static int currentUpdate = 20170226;
	private static ScheduledTask task;
	
	public static void load() {
		try {
			BungeeSuite.getInstance().updates.loadConfig();
		} catch(IOException e) {
			e.printStackTrace();
		}
		loadUpdates();
		loadPlayers();
		startTimer();
	}
	
	public static void reload() {
		stopTimer();
		UpdateUtils.updates = new HashMap<Integer, List<String>>();
		UpdateUtils.players = new HashMap<UUID, Integer>();
		load();
	}
	
	public static void loadUpdates() {
		for(final String dates : BungeeSuite.getInstance().updates.getConfig().getSection("Updates").getKeys())
			try {
				final int update = Integer.parseInt(dates.replaceAll("-", ""));
				if(UpdateUtils.updates.containsKey(update))
					continue;
				final List<String> changelog = BungeeSuite.getInstance().updates.getConfig().getStringList("Updates." + dates);
				UpdateUtils.updates.put(update, changelog);
				if(update <= UpdateUtils.currentUpdate)
					continue;
				UpdateUtils.currentUpdate = update;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
	}
	
	public static void loadPlayers() {
		for(final String key : BungeeSuite.getInstance().updates.getConfig().getSection("Players").getKeys()) {
			final UUID uuid = UUID.fromString(key);
			if(!UpdateUtils.players.containsKey(uuid)) {
				final int lastUpdate = BungeeSuite.getInstance().updates.getConfig().getInt("Players." + key);
				UpdateUtils.players.put(uuid, lastUpdate);
			}
		}
	}
	
	public static void readUpdate(final UUID uuid) {
		if(!hasPlayer(uuid)) {
			if(UpdateUtils.players.containsKey(uuid))
				UpdateUtils.players.remove(uuid);
			UpdateUtils.players.put(uuid, UpdateUtils.currentUpdate);
			BungeeSuite.getInstance().updates.getConfig().set("Players." + uuid.toString(), UpdateUtils.currentUpdate);
			try {
				BungeeSuite.getInstance().updates.saveConfig();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean hasPlayer(final UUID uuid) {
		return UpdateUtils.players.containsKey(uuid) && UpdateUtils.players.get(uuid) >= UpdateUtils.currentUpdate;
	}
	
	public static void check(final ProxiedPlayer p) {
		if(!hasPlayer(p.getUniqueId()))
			p.sendMessage(new TextComponent(Utils.toColor(
					"&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-\n                           &c&l[UPDATE]\n\n&f&lThere is a new update you have not seen! &e&l(/updates)\n&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-&d&l-&b&l-")));
	}
	
	public static void check() {
		for(final ProxiedPlayer pl : BungeeCord.getInstance().getPlayers())
			check(pl);
	}
	
	public static void startTimer() {
		UpdateUtils.task = BungeeCord.getInstance().getScheduler().schedule(BungeeSuite.getInstance(), () -> UpdateUtils.check(), 0L, 30L,
				TimeUnit.MINUTES);
	}
	
	public static void stopTimer() {
		if(UpdateUtils.task != null)
			UpdateUtils.task.cancel();
	}
}