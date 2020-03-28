package it.notreference.bungee.premiumlogin.api.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
//import it.notreference.bungee.premiumlogin.api.PremiumLoginEvent;
import net.md_5.bungee.api.plugin.Event;

/**
 * PremiumLogin 1.5 by NotReference
 *
 * @eventfiredesc This event will be fired if a premium player quit the network.
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
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
