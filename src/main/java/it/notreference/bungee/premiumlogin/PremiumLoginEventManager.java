package it.notreference.bungee.premiumlogin;

import net.md_5.bungee.api.plugin.Event;

/**
 *
 * PremiumLogin 1.6.2 By NotReference
 *
 * @author NotReference
 * @version 1.6.2
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
	public static void fire(Event ev) {

		PremiumLoginMain.i().getProxy().getPluginManager().callEvent(ev);

	}
	
}
