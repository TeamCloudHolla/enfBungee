package com.enforcedmc.bungeecord.objects.players;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class PlayerRegistry {
	private static final ConcurrentHashMap<UUID, Player> PLAYERS = new ConcurrentHashMap<UUID, Player>();
	
	public static final Player register(Player player) {
		return getPlayers().put(player.getUUID(), player);
	}
	
	public static final Player deregister(Player player) {
		return getPlayers().remove(player.getUUID());
	}
	
	public static final boolean isPlayer(UUID uuid) {
		return getPlayers().containsKey(uuid);
	}
	
	public static Player getByUUID(UUID uuid) {
		return getPlayers().getOrDefault(uuid, new Player(uuid));
	}
	
	public static Player getByProxyPlayer(ProxiedPlayer proxiedPlayer) {
		return getPlayers().getOrDefault(proxiedPlayer.getUniqueId(), new Player(proxiedPlayer.getUniqueId()));
	}
	
	public static final ConcurrentHashMap<UUID, Player> getPlayers() {
		return PLAYERS;
	}
	
	public static Collection<? extends Player> getRegisteredPlayers() {
		return getPlayers().values();
	}
	
	public static List<Player> getPlayers1() {
		return ImmutableList.copyOf(PLAYERS.values());
	}
	
	public static Player getPlayer(String string) {
		return PLAYERS.get(string);
	}
}