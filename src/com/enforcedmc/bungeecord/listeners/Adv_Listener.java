package com.enforcedmc.bungeecord.listeners;

import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Adv_Listener implements Listener {
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onChat(final ChatEvent e) {
		final ProxiedPlayer player = (ProxiedPlayer) e.getSender();
		String message = e.getMessage().toLowerCase();
		if(player.hasPermission("enf.staff") || player.hasPermission("enf.url") || message.contains("enforced") || message.contains("youtube")
				|| message.contains("gyazo") || message.contains("imgur") || message.startsWith("/mail"))
			return;
		if(Utils.checkForIp(message) || Utils.checkForDomain(message) || message.contains(",com") || message.contains("(.)") || message.contains(".zone")
				|| message.contains(",zone") || message.contains(".zone") || message.contains("(dot)zone") || message.contains(" . de")
				|| message.contains("( . ) de") || message.contains("(dot) de") || message.contains("( dot ) de") || message.contains("(dot)com")
				|| message.contains("[dot]com") || message.contains("{dot}com") || message.contains("<dot>com") || message.contains("dotcom")
				|| message.contains("(.)") || message.contains(".(com)") || message.contains("\u24d2") || message.contains(",net")
				|| message.contains(".net") || message.contains(" . com") || message.contains("{dot}net") || message.contains("[dot]net")
				|| message.contains("<dot>net") || message.contains("(dot)net") || message.contains("dotnet") || message.contains("\u24dd")
				|| message.contains("\u24de") || message.contains("\uff43") || message.contains("\uff4e") || message.contains(",org")
				|| message.contains(".org") || message.contains("(dot)org") || message.contains("[dot]org") || message.contains("{dot}org")
				|| message.contains("<dot>org") || message.contains("dotorg") || message.contains("(,)") || message.contains(",co")
				|| message.contains(".co") || message.contains("(dot)co") || message.contains("[dot]co") || message.contains("{dot}co")
				|| message.contains("<dot>co") || message.contains("dotco") || message.contains(",de") || message.contains(".de")
				|| message.contains("(dot)de") || message.contains("[dot]de") || message.contains("{dot}de") || message.contains("<dot>de")
				|| message.contains("dotde") || message.contains(",fr") || message.contains(".fr") || message.contains("<.> net")
				|| message.contains("<.>net") || message.contains("<.>com") || message.contains("<,>net") || message.contains("<,> net")
				|| message.contains("<.> com") || message.contains("(dot)fr") || message.contains("[dot]fr") || message.contains("{dot}fr")
				|| message.contains("<dot>fr") || message.contains("dotfr") || message.contains(" . fr") || message.contains("( . ) fr")
				|| message.contains("(dot) fr") || message.contains("( dot ) fr") || message.contains("<,>com") || message.contains("<,> com")
				|| message.contains("<>com") || message.contains("<> com") || message.contains("<> net") || message.contains("<>net")
				|| message.contains("*com") || message.contains("*net") || message.contains("*org") || message.contains("* com")
				|| message.contains("* net") || message.contains("* org") || message.contains("(dawt)") || message.contains("{dawt}")
				|| message.contains("dot com") || message.contains("dot org") || message.contains("dot net") || message.contains("{dot}")) {
			e.setCancelled(true);
			message = e.getMessage();
			for(final ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
				if(players.hasPermission("enf.staff")) {
					players.sendMessage(new TextComponent("§8§l■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"));
					players.sendMessage(new TextComponent("§a§l§nADVERTISEMENT: "));
					players.sendMessage(new TextComponent(""));
					players.sendMessage(new TextComponent("§6" + player.getName() + " §7seems to be advertising!"));
					players.sendMessage(Utils.toColor("§7Advertisement: §c§l" + message));
					players.sendMessage(new TextComponent(""));
					players.sendMessage(
							new TextComponent("§cSERVER: §7" + ProxyServer.getInstance().getPlayer(player.getName()).getServer().getInfo().getName()));
					players.sendMessage(new TextComponent(""));
					final TextComponent component2 = new TextComponent("§c§l[CLICK TO BAN]");
					component2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder(
									"§c§lBAN: §f§l" + player.getName() + "\n§c§lCOMMAND: §f§l/gban " + player.getName() + " Advertising: " + message)
											.create()));
					component2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://shop.enforcedmc.com"));
					players.sendMessage(component2);
					players.sendMessage(new TextComponent("§8§l■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"));
				}
		}
	}
}