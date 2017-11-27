package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.listeners.PingListener;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class SPing_Command extends Command {
	
	public SPing_Command() {
		super("sping", "enf.admin", "pingreload", "reloadping", "serverping");
	}
	
	@Override
	public void execute(final CommandSender cs, final String[] args) {
		PingListener.load();
		cs.sendMessage(new TextComponent("§aYou success reloaded the ServerPing."));
	}
}