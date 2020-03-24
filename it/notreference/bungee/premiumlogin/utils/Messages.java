package it.notreference.bungee.premiumlogin.utils;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * PremiumLogin 1.2 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class Messages {

	
	//Da ora send e sendParseColors sono uguali.
	public static void send(ProxiedPlayer p, String m) {
		String nuovoMessaggio = m.replace("&", "§");
		p.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	public static void send(CommandSender c, String m) {
		String nuovoMessaggio = m.replace("&", "§");
		c.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	
	public static String parse(String f) {
		String culo = f.replace("&", "§");
		return culo;
	}
	
	public static void sendParseColors(ProxiedPlayer p, String m) {
		String nuovoMessaggio = m.replace("&", "§");
		p.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	public static void logConsole(String d) {
		PremiumLoginMain.i().getLogger().info("LOG - " + d);
	}
	
	public static void logStaff(String s, PlaceholderConf conf) {
		if(!ConfigUtils.getConfBool("log-staff")) {
			return;
		}
		for(ProxiedPlayer staff: PremiumLoginMain.i().getProxy().getPlayers()) {
			if(staff.hasPermission("premiumlogin.staff")) {
				String placeholderModifica1 = s.replace("[utente]", conf.getName());
				String placeholderModifica2 = placeholderModifica1.replace("[uuid]", conf.getUUID().toString());
				String placeholderFinale = placeholderModifica2.replace("[ip]", conf.ip());
				sendParseColors(staff, placeholderFinale);
			}
		}
	}
	
	

	

	
}
