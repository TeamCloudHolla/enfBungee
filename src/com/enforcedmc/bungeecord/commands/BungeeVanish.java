package com.enforcedmc.bungeecord.commands;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import com.enforcedmc.bungeecord.utils.*;
import net.md_5.bungee.api.chat.*;
import java.util.*;

public class BungeeVanish extends Command
{
    public static ArrayList<ProxiedPlayer> vanish;
    
    static {
        BungeeVanish.vanish = new ArrayList<ProxiedPlayer>();
    }
    
    public BungeeVanish() {
        super("bvanish", "enf.admin", new String[] { "bungeevanish" });
    }
    
    public void execute(final CommandSender s, final String[] args) {
        final ProxiedPlayer pl = (ProxiedPlayer)s;
        if (BungeeVanish.vanish.contains(pl)) {
            BungeeVanish.vanish.remove(pl);
            for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.hasPermission("enf.staff")) {
                    player.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&c&l(*) &a" + pl.getName() + " is no longer vanished from bungee!")));
                }
            }
            return;
        }
        if (!BungeeVanish.vanish.contains(pl)) {
            BungeeVanish.vanish.add(pl);
            for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.hasPermission("enf.staff")) {
                    player.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&c&l(*) &a" + pl.getName() + " is now vanished from bungee!")));
                }
            }
        }
    }
}
