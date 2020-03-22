package it.notreference.bungee.premiumlogin.utils;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * PremiumLogin 1.1 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class UUIDUtils {

	public static boolean isPremium(UUID ud) {
		ProxiedPlayer prox = PremiumLoginMain.i().getProxy().getPlayer(ud);
		if(prox == null) {
			throw new RuntimeException("This player is not online.");
		}
		if(getPremiumUUID(prox.getName()) == "NO_PREMIUM") {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isPremiumConnectionLegacy(ProxiedPlayer p) {
		PendingConnection con = p.getPendingConnection();
		if(con.isOnlineMode() && con.isLegacy()) { //isLegacy() -> Verifichiamo se si sta utilizzando il sistema di autenticazione più vecchio.
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isPremiumConnection(ProxiedPlayer p) {
		PendingConnection con = p.getPendingConnection();
		if(con.isOnlineMode() && !con.isLegacy()) { 
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isPremium(ProxiedPlayer p) {
		if(!p.isConnected()) {
			throw new RuntimeException("This player is not online.");
		}
		if(getPremiumUUID(p.getName()) == "NO_PREMIUM") {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isPremium(String name) {
		if(getPremiumUUID(name) == "NO_PREMIUM") {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getCrackedUUID(String name) {
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()).toString();
	}
	
	public static String getPremiumUUID(String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			Scanner scanner = new Scanner(url.openStream());
			String line = scanner.nextLine();
			scanner.close();
			return line.split("\"")[3];
		} catch (Exception ex) {
			return "NO_PREMIUM";
		}
	}
	
}
