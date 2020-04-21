package it.notreference.bungee.premiumlogin.utils.authentication;

import java.util.UUID;

import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 *
 * PremiumLogin 1.6.3 By NotReference
 *
 * @author NotReference
 * @version 1.6.3
 * @destination BungeeCord
 *
 */

public class AuthenticationBuilder {

	private AuthType type;
	private ProxiedPlayer p;
	private String name;
	private TipoConnessione cont;
	private UUID unique;
	private ServerInfo info;
	
	
	public AuthenticationBuilder setName(String n) {
		name = n;
		return this;
	}
	
	public AuthenticationBuilder setConnectionType(TipoConnessione con) {
		cont = con;
		return this;
	}
	
	public AuthenticationBuilder setAuthType(AuthType typ) {
		type = typ;
		return this;
	}
	
	public AuthenticationBuilder setPlayer(ProxiedPlayer ple) {
		p = ple;
		return this;
	}

	public AuthenticationBuilder setUUID(UUID plUuid) {
		unique = plUuid;
		return this;
	}
	
	public AuthenticationBuilder setServer(ServerInfo serv) {
		info = serv;
		return this;
	}
	
	public AuthenticationKey build() {
		return new AuthenticationKey(unique, p, info, name, cont, type);
	}
	
	
}
