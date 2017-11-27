package com.enforcedmc.bungeecord.commands;

import net.md_5.bungee.api.plugin.*;
import com.enforcedmc.bungeecord.commands.handler.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import com.enforcedmc.bungeecord.utils.*;
import net.md_5.bungee.api.chat.*;
import com.enforcedmc.bungeecord.*;
import java.io.*;
import java.util.*;
import net.md_5.bungee.config.*;

@CommandTag(usage = "§c§lUSAGE: §7/sc <message>")
public class StaffChat_Command extends Command
{
    public static ArrayList<ProxiedPlayer> staffchat;
    
    static {
        StaffChat_Command.staffchat = new ArrayList<ProxiedPlayer>();
    }
    
    public StaffChat_Command() {
        super("sc", "enf.staff", new String[] { "staffchat" });
    }
    
    public void execute(final CommandSender cs, final String[] args) {
        if (cs instanceof ProxiedPlayer) {
            final ProxiedPlayer pl = (ProxiedPlayer)cs;
            if (args.length >= 1) {
                String messages = "";
                for (final String arg : args) {
                    messages = String.valueOf(messages) + "§d" + arg + " ";
                }
                Utils.broadcast(Utils.toColor("§7(§a§l" + pl.getServer().getInfo().getName().toUpperCase() + "§7) " + getGroup(pl) + "§7" + pl.getName() + ": " + messages.trim()), "enf.staff", false);
            }
            if (args.length == 0) {
                if (StaffChat_Command.staffchat.contains(pl)) {
                    StaffChat_Command.staffchat.remove(pl);
                    pl.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&c&lSC: &7You toggled staff chat &coff&7!")));
                    return;
                }
                if (!StaffChat_Command.staffchat.contains(pl)) {
                    StaffChat_Command.staffchat.add(pl);
                    pl.sendMessage((BaseComponent)new TextComponent(Utils.toColor("&c&lSC: &7You toggled staff chat &aon&7!")));
                }
            }
        }
    }
    
    public static String getGroup(final ProxiedPlayer p) {
        try {
            final Map<String, String> ranks = new HashMap<String, String>();
            ranks.put("owner", "&7(&4&lOwner&7) ");
            ranks.put("admin", "&7(&c&lAdmin&7) ");
            ranks.put("srstaff", "&7(&5&lMod&7) ");
            ranks.put("staff", "&7(&2&lHelper&7) ");
            ranks.put("jrmod", "&7(&b&lJrMod&7) ");
            ranks.put("srmod", "&7(&6&lSrMod&7) ");
            ranks.put("helper", "&7(&2&lHelper&7) ");
            ranks.put("helper+", "&7(&2&lHelper+&7) ");
            ranks.put("manager", "&7(&4&lManager&7) ");
            final File file = new File(BungeeSuite.getInstance().getDataFolder().getParentFile().getParent(), "config.yml");
            final Configuration config = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(file);
            if (config.getStringList("groups." + p.getName()) != null) {
                final String rank = config.getStringList("groups." + p.getName()).get(0);
                return ranks.get(rank);
            }
        }
        catch (IOException ex) {}
        return "";
    }
}
