package it.notreference.bungee.premiumlogin.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;

/**
 *
 * PremiumLogin 1.6.3 By NotReference
 *
 * @author NotReference
 * @version 1.6.3
 * @destination BungeeCord
 *
 */

public class ConfigUtils {

	public static String getConfStr(String id) {

		return PremiumLoginMain.i().getConfig().getString(id).replace("&", "§");
	}
	
	public static boolean getConfBool(String xd) {

		return PremiumLoginMain.i().getConfig().getBoolean(xd);
	}
	
	public static void set(String id, String val) {

		PremiumLoginMain.i().getConfig().set(id, val);
	}
	
	public static boolean permessoSettato() {
		if(!getConfStr("premium-permission").equalsIgnoreCase("nessuno")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getPremiumPerm() {

		return getConfStr("premium-permission");
	}
	
	public static void reload() {

		PremiumLoginMain.i().reloadConfig();
		PremiumLoginMain.i().setupAutoLanguage();
	}
	
	public static void salva()
	{
		PremiumLoginMain.i().saveConfig();
	}
	
	public static void player_reload() {

		PremiumLoginMain.i().reloadPlayerConf();
	}
	
	public static void player_save() {

		PremiumLoginMain.i().sPlayerConf();
	}
	
	public static boolean allowLegacy() {

		return getConfBool("allow-premium-legacy-connections");
	}
	
	public static boolean hasPremiumAutoLogin(ProxiedPlayer p) {
		if(PremiumLoginMain.i().getPlayersConf().getString("users."  + p.getName()).contains(new String("true").toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean hasPremiumAutoLogin(String e) {
		if(PremiumLoginMain.i().getPlayersConf().getString("users."  + e).contains(new String("true").toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void enablePremium(ProxiedPlayer p) {
		PremiumLoginMain.i().getPlayersConf().set("users." + p.getName() , "true");
		player_save();
		player_reload();
	}
	
	public static void disablePremium(ProxiedPlayer p) {
		PremiumLoginMain.i().getPlayersConf().set("users." + p.getName() , "false");
		player_save();
		player_reload();
	}
	
	public static void enablePremium(String p) {
		PremiumLoginMain.i().getPlayersConf().set("users." + p , "true");
		player_save();
		player_reload();
	}
	
	public static void disablePremium(String p) {
		PremiumLoginMain.i().getPlayersConf().set("users." + p , "false");
		player_save();
		player_reload();
	}
}
