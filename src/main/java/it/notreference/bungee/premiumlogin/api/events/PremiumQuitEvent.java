package it.notreference.bungee.premiumlogin.api.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
//import it.notreference.bungee.premiumlogin.api.PremiumLoginEvent;
import net.md_5.bungee.api.plugin.Event;

/**
 *
 * PremiumLogin 1.6.1 By NotReference
 *
 * @author NotReference
 * @version 1.6.1
 * @destination BungeeCord
 *
 */

/**
 * 
 * @since 1.1.3
 * 
 */
public class PremiumQuitEvent extends Event //implements PremiumLoginEvent
{
	
	private ProxiedPlayer p;
	
	public PremiumQuitEvent(ProxiedPlayer p) {
		this.p = p;
	}
	
	public ProxiedPlayer getPlayer() {
		return p;
	}
	
	public String getName() {
		return p.getName();
	}
	

}
