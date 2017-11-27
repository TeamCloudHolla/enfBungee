package com.enforcedmc.bungeecord.objects.players;

import java.util.Optional;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class Player {
	private final UUID uuid;
	private boolean Ghost;
	private long report_stamp;
	private ProxiedPlayer players;
	
	public Player(UUID uuid) {
		this.uuid = uuid;
		Ghost = false;
		report_stamp = 0L;
	}
	
	public void Players(final ProxiedPlayer players) {
		this.players = players;
	}
	
	public ProxiedPlayer getPlayers() {
		return players;
	}
	
	public final UUID getUUID() {
		return uuid;
	}
	
	public final Optional<ProxiedPlayer> getPlayer() {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(getUUID());
		if(player == null)
			return Optional.empty();
		return Optional.of(player);
	}
	
	public boolean isGhost() {
		return Ghost;
	}
	
	public void setGhost(boolean Ghost) {
		this.Ghost = Ghost;
	}
	
	public final boolean isStaff() {
		return ProxyServer.getInstance().getPlayer(uuid).hasPermission("enf.staff");
	}
	
	public long getReportStamp() {
		return report_stamp;
	}
	
	public void setReportStamp(long seconds) {
		report_stamp = System.currentTimeMillis() + 1000L * seconds;
	}
	
	public boolean canReport() {
		return System.currentTimeMillis() > getReportStamp();
	}
}
