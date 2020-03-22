package it.notreference.bungee.premiumlogin;

import net.md_5.bungee.api.plugin.Event;
//import it.notreference.bungee.premiumlogin.api.PremiumLoginEvent;

public class PremiumLoginEventManager {

	public static void fire(Event ev) {
		//PremiumLoginEvent event = (PremiumLoginEvent) ev;
		//if(event == null) {
			//return;
		//}
		PremiumLoginMain.i().getProxy().getPluginManager().callEvent(ev);
	}
	
}
