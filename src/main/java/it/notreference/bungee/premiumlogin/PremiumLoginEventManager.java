package it.notreference.bungee.premiumlogin;

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

public class PremiumLoginEventManager {

	/**
	 *
	 * Fires an event.
	 *
	 * @param ev the event
	 */
	public static Event fire(Event ev) {

		return PremiumLoginMain.i().getProxy().getPluginManager().callEvent(ev);

	}
	
}
