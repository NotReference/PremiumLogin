package it.notreference.bungee.premiumlogin;

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
 * 
 * @since 1.1
 *
 */
public class EventManager {

	/**
	 * Fires an event.
	 * @param e The event to be fired.
	 * 
	 */
	public static void fire(Event e) {
		PremiumLoginMain.i().getProxy().getPluginManager().callEvent(e);
	
	}
	
}
