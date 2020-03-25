package it.notreference.bungee.premiumlogin.utils;

import java.util.UUID;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * PremiumLogin 1.4 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class AuthenticationKey {

	private UUID ud;
	private ProxiedPlayer prp;
	private ServerInfo cur;
	private String nome;
	private TipoConnessione c;
	private AuthType td;
	
	public AuthenticationKey(UUID uuid, ProxiedPlayer p, ServerInfo currentServer, String name, TipoConnessione con, AuthType t) {
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
	
	public TipoConnessione getConType() {
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
