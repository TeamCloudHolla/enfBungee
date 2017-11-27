package com.enforcedmc.bungeecord.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.enforcedmc.bungeecord.BungeeSuite;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {
	public static ServerPing.PlayerInfo[] pi = new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo(
			ChatColor.translateAlternateColorCodes('&',
					"&f&l*&b&l*&f&l* &e&lEnforced&6&lMC &f&l*&b&l*&f&l*%nl%%nl%&eWebsite: &7www.enforcedmc.com%nl%&eStore: &7www.enforcedmc.com/shop%nl%&eTeamspeak: &7ts.enforcedmc.net"),
			"1000")};
	public static String version = "&c50% OFF [SALE]&r  &7%online%&8/&71000";
	public static String desc = "                         &f&l*&b&l*&f&l* &e&lEnforced&6&lMC &f&l*&b&l*&f&l*        %nl%&e&l* &7Visit Our Store: &6www.enforcedmc.com/shop";
	public static Favicon ic;

	@EventHandler(priority = 64)
	public void onServerPing(final ProxyPingEvent e) {
		final ServerPing conn = e.getResponse();
		List<String> servers = new ArrayList<String>();
		for(ServerInfo si : BungeeCord.getInstance().getServers().values())
			servers.add(si.getName());
		String copy = pi[0].getName();
		copy = copy.replace("%online%", BungeeCord.getInstance().getPlayers().size() + "");
		for(String server : servers)
			copy = copy.replace("%" + server.toLowerCase() + "%", BungeeCord.getInstance().getServerInfo(server).getPlayers().size() + "");
		conn.setVersion(new ServerPing.Protocol(version.replace("%online%", BungeeCord.getInstance().getPlayers().size() + "").replace("%max%",
				BungeeCord.getInstance().getPlayers().size() + 1 + ""), 999));
		conn.setDescription(desc);
		conn.setPlayers(new ServerPing.Players(1000, BungeeCord.getInstance().getOnlineCount(),
				new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo(copy, "1000")}));
		if(ic != null)
			conn.setFavicon(ic);
	}

	public static void load() {
		try {
			BungeeSuite.getInstance().options.loadConfig();
		} catch(IOException e) {
			e.printStackTrace();
		}
		pi = new ServerPing.PlayerInfo[]{
				new ServerPing.PlayerInfo(translate(BungeeSuite.getInstance().getOptions().getString("Ping.PlayerInfo")).replaceAll("%nl%", "\n"), "1000")};
		version = translate(BungeeSuite.getInstance().getOptions().getString("Ping.Version")).replaceAll("%nl%", "\n");
		desc = translate(BungeeSuite.getInstance().getOptions().getString("Ping.Description")).replaceAll("%nl%", "\n");
		if(new File(BungeeSuite.getInstance().getDataFolder() + "/favicon.png").exists())
			try {
				ic = Favicon.create(ImageIO.read(new File(BungeeSuite.getInstance().getDataFolder() + "/favicon.png")));
			} catch(IOException ex) {}
	}

	public static String translate(final String input) {
		String output = input;
		try {
			String code;
			for(output = ChatColor.translateAlternateColorCodes('&', input); output
					.contains("%unicode"); output = output.replaceAll("%unicode" + code + "%", String.valueOf(Integer.parseInt(code, 16))))
				code = output.split("%unicode")[1].split("%")[0];
		} catch(Exception ex) {}
		return output;
	}
}