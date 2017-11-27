package com.enforcedmc.bungeecord.commands;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import com.enforcedmc.bungeecord.utils.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.*;

public class HubCount extends Command
{
    public HubCount() {
        super("hubcount", "enf.admin", new String[] { "counthubs" });
    }
    
    public void execute(final CommandSender s, final String[] args) {
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&a&l(*) The Hub Counts Are...")));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub1: &f" + BungeeCord.getInstance().getServerInfo("Hub1").getPlayers().size())));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub2: &f" + BungeeCord.getInstance().getServerInfo("Hub2").getPlayers().size())));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub3: &f" + BungeeCord.getInstance().getServerInfo("Hub3").getPlayers().size())));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub4: &f" + BungeeCord.getInstance().getServerInfo("Hub4").getPlayers().size())));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub5: &f" + BungeeCord.getInstance().getServerInfo("Hub5").getPlayers().size())));
        s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&eHub6: &f" + BungeeCord.getInstance().getServerInfo("Hub6").getPlayers().size())));
    }
}
