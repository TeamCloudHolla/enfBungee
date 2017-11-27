package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.utils.UpdateUtils;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Updates_Command extends Command {
	
	public Updates_Command() {
		super("updates");
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(final CommandSender s, final String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("reload") && s.hasPermission("updates.reload")) {
			UpdateUtils.reload();
			s.sendMessage(new TextComponent(Utils.toColor("&aYou success reloaded the Updates.")));
		} else {
			int update = UpdateUtils.currentUpdate;
			if(args.length == 1 && isInt(args[0]) && UpdateUtils.updates.containsKey(Integer.valueOf(args[0])))
				update = Integer.parseInt(args[0]);
			s.sendMessage(new TextComponent(Utils.toColor("                 &e&l*&6&l* &b&lUPDATES &e&l*&6&l* &a(" + formatDate(update) + ")")));
			for(final String change : UpdateUtils.updates.get(update))
				s.sendMessage(Utils.toColor(change));
			if(update == UpdateUtils.currentUpdate && s instanceof ProxiedPlayer) {
				final ProxiedPlayer p = (ProxiedPlayer) s;
				UpdateUtils.readUpdate(p.getUniqueId());
			}
		}
	}

	private boolean isInt(final String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(Exception ex) {
			return false;
		}
	}

	private String formatDate(final int i) {
		String out = "";
		final String i2 = new StringBuilder().append(i).toString();
		try {
			out = String.valueOf(i2.substring(6)) + "." + i2.substring(4, 6) + "." + i2.substring(0, 4);
		} catch(Exception ex) {}
		return out;
	}
}