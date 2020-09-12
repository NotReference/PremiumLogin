package it.notreference.bungee.premiumlogin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;




/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class UUIDUtils {

	public static boolean isPremium(UUID ud) {
		ProxiedPlayer prox = PremiumLoginMain.i().getProxy().getPlayer(ud);
		if(prox == null) {
			throw new RuntimeException("This player is not online.");
		}
		if(getPremiumUUID(prox.getName()) == "UNABLE_O_NO_PREMIUM") {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isPremiumConnectionLegacy(ProxiedPlayer p) {
		PendingConnection con = p.getPendingConnection();
		if(con.isOnlineMode() && con.isLegacy()) {
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
		return !getPremiumUUID(p.getName()).equals("UNABLE_O_NO_PREMIUM");
	}
	
	public static boolean isPremium(String name) {
		if(getPremiumUUID(name) == "UNABLE_O_NO_PREMIUM") {
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
			JsonObject obj = new Gson().fromJson(line, JsonObject.class);
		    return obj.get("id").getAsString();
		} catch (Exception ex) {
			return "UNABLE_O_NO_PREMIUM";
		}
	}
	
}
