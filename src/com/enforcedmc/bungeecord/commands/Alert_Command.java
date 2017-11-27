package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.commands.handler.CommandTag;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

@CommandTag(usage = "§c§lUSAGE: §7/alert <message>")
public class Alert_Command extends Command {
	
	public Alert_Command() {
		super("alert", "enf.admin");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length >= 1) {
			StringBuilder str = new StringBuilder();
			for(String arg : args)
				str.append(arg).append(" ");
			/*if(BungeeSuite.getInstance().getCache().getString("alert_format") != null)
				Utils.broadcast(Utils.toColor(BungeeSuite.getInstance().getCache().getString("alert_format").replace("%msg%", str.toString().trim())),
						null, false);
			else*/
			Utils.broadcast(Utils.toColor("&e&l(!) &f&l" + str.toString().trim()), null, false);
		} else
			sender.sendMessage(new TextComponent(getClass().getDeclaredAnnotation(CommandTag.class).usage()));
	}
}