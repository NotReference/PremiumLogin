package it.notreference.bungee.premiumlogin.utils;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * PremiumLogin 1.1 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PlaceholderConf {

	private String name;
	private UUID ud;
	private String ipp; //inetsocketaddress
	
	public PlaceholderConf(String nick, UUID uuid, String ip) {
		name = nick;
		ud = uuid;
		ipp = ip;
	}
	
	@Deprecated
	public PlaceholderConf(String nick, UUID uuid, InetSocketAddress ip) {
		name = nick;
		ud = uuid;
		ipp = ip.getHostString();
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getUUID() {
		return ud;
	}
	
	public String ip() {
		return ipp;
	}
	
	
}
