package com.enforcedmc.bungeecord.objects.balancer;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;

public final class BalanceLoader {
	private List<ServerInfo> HUBS;

	public BalanceLoader() {
		if(HUBS == null)
			HUBS = getAllHubs();
	}

	public List<ServerInfo> getAllHubs() {
		List<ServerInfo> servers = new ArrayList<ServerInfo>();
		for(ServerInfo server : BungeeCord.getInstance().getServers().values())
			if(isHub(server))
				servers.add(server);
		return servers;
	}

	public final List<ServerInfo> getHubs() {
		return HUBS;
	}

	public final ServerInfo getLowestHub() {
		//return getHubs().get(new Random().nextInt(getHubs().size()));
		ServerInfo info = null;
		for(ServerInfo serverInfo : getHubs())
			if(info == null || info.getPlayers().size() > serverInfo.getPlayers().size())
				info = serverInfo;
		return info;
		
		/*ServerInfo lowest = BungeeCord.getInstance().getServerInfo("Hub6") != null
				? BungeeCord.getInstance().getServerInfo("Hub6")
				: BungeeCord.getInstance().getServerInfo("Hub2");
		if(current.getPlayers().size() == 1)
			return current;
		for(ServerInfo info : getHubs())
			if(lowest.getPlayers().size() > info.getPlayers().size())
				lowest = info;
		return lowest;*/
	}

	public boolean isHub(ServerInfo info) {
		if(info.getName().startsWith("Hub"))
			return true;
		return false;
	}
}