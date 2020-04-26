package it.notreference.bungee.premiumlogin.utils.authentication;

import java.util.UUID;

import it.notreference.bungee.premiumlogin.utils.ConnectionType;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;



/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public class AuthenticationKey {

	private UUID ud;
	private ProxiedPlayer prp;
	private ServerInfo cur;
	private String nome;
	private ConnectionType c;
	private AuthType td;
	
	public AuthenticationKey(UUID uuid, ProxiedPlayer p, ServerInfo currentServer, String name, ConnectionType con, AuthType t) {
		nome = name;
		ud = uuid;
		prp = p;
		cur = currentServer;
		 c = con;
		 td = t;
	}
	
	public AuthType getAuthType() {
		return td;
	}
	
	public String getName() {
		return nome;
	}
	
	public ConnectionType getConType() {
		return c;
	}
	
	public UUID getUUID() {
		return ud;
	}
	
	public ProxiedPlayer player() {
		return prp;
	}
	
	public ServerInfo playerServer() {
		return cur;
	}
	
}
