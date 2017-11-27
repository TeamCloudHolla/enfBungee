package com.enforcedmc.bungeecord.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.enforcedmc.bungeecord.BungeeSuite;
import com.enforcedmc.bungeecord.utils.Utils;

public class KickAll_Command extends Command
{
    
    public KickAll_Command() {
        super("gkickall", "enf.admin", "gkickhubs");
    }
    
    public void execute(final CommandSender s, final String[] args) {
            final ProxiedPlayer pl = (ProxiedPlayer) s;
            final ServerInfo info = pl.getServer().getInfo();
            final ServerInfo hub = BungeeSuite.getInstance().getBalanceLoader().getLowestHub();
            s.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&a&l(*) You sent the server to hubs!")));
            info.getPlayers().stream().filter(player -> !player.hasPermission("enf.staff")).forEach(player -> {
                player.connect(hub);
                player.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&c&l(*) &cYou've been transferred to the &7hub&c.")));
            });
            return;
    }
}