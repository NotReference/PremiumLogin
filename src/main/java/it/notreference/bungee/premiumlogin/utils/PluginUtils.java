package it.notreference.bungee.premiumlogin.utils;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


/**
 *
 * PremiumLogin 1.6.5 By NotReference
 *
 * @author NotReference
 * @version 1.6.5
 * @destination BungeeCord
 *
 */

public class PluginUtils {


	/*

	Alias of sendParseColors.

	 */

	/*

	1.6.5 Spanish N Support.

	 */

	/*

	á, é, í, ó, ú

     ñ

	 */

	public static void send(ProxiedPlayer p, String m) {
		String nuovoMessaggio = m.replace("&", "§");

		if(nuovoMessaggio.contains(".N.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "ñ");
		}
		if(nuovoMessaggio.contains(".e1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "è");
		}

		if(nuovoMessaggio.contains(".e2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "é");
		}

		if(nuovoMessaggio.contains(".a1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "à");
		}

		if(nuovoMessaggio.contains(".a2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "á");
		}

		if(nuovoMessaggio.contains(".i1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ì");
		}

		if(nuovoMessaggio.contains(".i2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "í");
		}

		if(nuovoMessaggio.contains(".o1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ò");
		}

		if(nuovoMessaggio.contains(".o2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ó");
		}

		if(nuovoMessaggio.contains(".u1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ù");
		}

		if(nuovoMessaggio.contains(".u2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ú");
		}

		p.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	public static void send(CommandSender c, String m) {
		String nuovoMessaggio = m.replace("&", "§");

		if(nuovoMessaggio.contains(".N.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "ñ");
		}
		if(nuovoMessaggio.contains(".e1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "è");
		}

		if(nuovoMessaggio.contains(".e2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "é");
		}

		if(nuovoMessaggio.contains(".a1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "à");
		}

		if(nuovoMessaggio.contains(".a2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "á");
		}

		if(nuovoMessaggio.contains(".i1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ì");
		}

		if(nuovoMessaggio.contains(".i2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "í");
		}

		if(nuovoMessaggio.contains(".o1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ò");
		}

		if(nuovoMessaggio.contains(".o2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ó");
		}

		if(nuovoMessaggio.contains(".u1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ù");
		}

		if(nuovoMessaggio.contains(".u2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ú");
		}

		c.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	
	public static String parse(String f) {

			/*

	á, é, í, ó, ú

     ñ

	 */

		String nuovoMessaggio = f.replace("&", "§");


		if(nuovoMessaggio.contains(".N.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "ñ");
		}
		if(nuovoMessaggio.contains(".e1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "è");
		}

		if(nuovoMessaggio.contains(".e2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "é");
		}

		if(nuovoMessaggio.contains(".a1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "à");
		}

		if(nuovoMessaggio.contains(".a2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "á");
		}

		if(nuovoMessaggio.contains(".i1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ì");
		}

		if(nuovoMessaggio.contains(".i2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "í");
		}

		if(nuovoMessaggio.contains(".o1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ò");
		}

		if(nuovoMessaggio.contains(".o2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ó");
		}

		if(nuovoMessaggio.contains(".u1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ù");
		}

		if(nuovoMessaggio.contains(".u2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ú");
		}

		return nuovoMessaggio;
	}
	
	public static void sendParseColors(ProxiedPlayer p, String m) {
		String nuovoMessaggio = m.replace("&", "§");

		if(nuovoMessaggio.contains(".N.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "ñ");
		}
		if(nuovoMessaggio.contains(".e1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "è");
		}

		if(nuovoMessaggio.contains(".e2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "é");
		}

		if(nuovoMessaggio.contains(".a1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "à");
		}

		if(nuovoMessaggio.contains(".a2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "á");
		}

		if(nuovoMessaggio.contains(".i1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ì");
		}

		if(nuovoMessaggio.contains(".i2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "í");
		}

		if(nuovoMessaggio.contains(".o1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ò");
		}

		if(nuovoMessaggio.contains(".o2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ó");
		}

		if(nuovoMessaggio.contains(".u1.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ù");
		}

		if(nuovoMessaggio.contains(".u2.")) {
			nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "ú");
		}

		p.sendMessage(new TextComponent(nuovoMessaggio));
	}
	
	public static void logConsole(String d) {
		PremiumLoginMain.i().getLogger().info("LOG - " + parse(d));
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
