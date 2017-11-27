package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.commands.handler.CommandTag;
import com.enforcedmc.bungeecord.objects.players.Player;
import com.enforcedmc.bungeecord.objects.players.PlayerRegistry;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

@CommandTag(usage = "§c§lUSAGE: §7/report <player> <reason>")
public class Report_Command extends Command {
	
	public Report_Command() {
		super("report");
	}

	@Override
	public void execute(final CommandSender s, final String[] args) {
		if(!(s instanceof ProxiedPlayer))
			return;
		final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) s;
		final Player player = PlayerRegistry.getByProxyPlayer(proxiedPlayer);
		if(args.length > 1) {
			if(!player.canReport())
				proxiedPlayer.sendMessage(new TextComponent(Utils
						.toColor("&c&lREPORT: &fYou cannot use the report function for &e" + Utils.formatSeconds(player.getReportStamp(), true) + "&f.")));
			else {
				final ProxiedPlayer proxiedTarget = ProxyServer.getInstance().getPlayer(args[0]);
				final String reason = Utils.getMessage(1, args);
				if(proxiedTarget == null)
					proxiedPlayer.sendMessage(new TextComponent(Utils.toColor("&c&lREPORT: &e" + args[0] + " &fisn't online!")));
				else if(proxiedTarget.getName().equalsIgnoreCase(proxiedPlayer.getName()))
					proxiedPlayer.sendMessage(new TextComponent(Utils.toColor("&c&lREPORT &fYou cannot report yourself!")));
				else {
					player.setReportStamp(15L);
					proxiedPlayer.sendMessage(new TextComponent(Utils.toColor("&c&lREPORT &fYour report has been successfully submitted.")));
					Utils.broadcast("§8§l«»■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■«»\n§e§lREPORTER: §f" + proxiedPlayer.getName() + "\n§e§lOFFENDER: §f"
							+ proxiedTarget.getName() + "\n§e§lREASON: §f" + reason + "\n§e§lSERVER: §f" + proxiedTarget.getServer().getInfo().getName()
							+ "\n§8§l«»■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■«»", "enf.staff", false);
				}
			}
		} else
			proxiedPlayer.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
	}
}