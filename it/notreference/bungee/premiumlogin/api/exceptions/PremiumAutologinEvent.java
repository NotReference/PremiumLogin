package it.notreference.bungee.premiumlogin.api.exceptions;

import it.notreference.bungee.premiumlogin.utils.AuthType;
import it.notreference.bungee.premiumlogin.utils.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;

import java.util.UUID;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
//import it.notreference.bungee.premiumlogin.api.PremiumLoginEvent;
import net.md_5.bungee.api.plugin.Event;

/**
 * PremiumLogin 1.1 by NotReference
 *
 * @eventfiredesc This event will be fired if a premium player has been forcelogged.
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumAutologinEvent extends Event //implements PremiumLoginEvent
{

	private AuthenticationKey authkey;
	private String n;
	private PendingConnection c;
	private UUID u;
	private ProxiedPlayer ply;
	
	public PremiumAutologinEvent(ProxiedPlayer p, String name, PendingConnection con, UUID uni, AuthenticationKey k) {
		ply = p;
		u = uni;
		c = con;
		n = name;
		authkey = k;
	}
	
	public AuthType getAuthType() {
		return authkey.getAuthType();
	}
	
	public TipoConnessione getConnectionType() {
		return authkey.getConType();
	}
	
	public UUID getUUID() {
		return u;
	}
	
	public ProxiedPlayer getPlayer() {
		return ply;
	}
	
	public PendingConnection getConnection() {
		return c;
	}
	
	public String getName() {
		return n;
	}
	
}
