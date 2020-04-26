package it.notreference.bungee.premiumlogin.api.events;

import it.notreference.bungee.premiumlogin.utils.ConnectionType;

import java.net.InetSocketAddress;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
//import it.notreference.bungee.premiumlogin.api.PremiumLoginEvent;
import net.md_5.bungee.api.plugin.Event;



/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */


public class PremiumJoinEvent extends Event //implements PremiumLoginEvent
{
	
	private ProxiedPlayer player;
	private String n;
	private UUID u;
	private InetSocketAddress i;
	private ConnectionType conn;
	
	public PremiumJoinEvent(ProxiedPlayer p, String name, UUID uuid, InetSocketAddress ip, ConnectionType con) {
		player = p;
		n = name;
		u = uuid;
		i = ip;
		conn = con;
	}
	
	public ProxiedPlayer getPlayer() {
		return player;
	}
	
	public String getName() {
		return n;
	}
	
	public UUID getUUID() {
		return u;
	}
	
	public InetSocketAddress getIP() {
		return i;
	}
	
	public ConnectionType getConnectionType() {
		return conn;
	}

}
