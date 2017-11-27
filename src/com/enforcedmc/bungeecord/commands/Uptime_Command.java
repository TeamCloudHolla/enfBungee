package com.enforcedmc.bungeecord.commands;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Uptime_Command extends Command {
	
	public Uptime_Command() {
		super("uptime", "enf.admin");
	}

	@Override
	public void execute(CommandSender s, String[] args) {
		long time = System.currentTimeMillis() - BungeeSuite.getInstance().start_time;
		s.sendMessage(new TextComponent("§f§lUPTIME: §eThe server has been up for §6" + Utils.getTime(time) + "§e."));
	}
}