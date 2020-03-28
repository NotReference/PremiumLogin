package it.notreference.bungee.premiumlogin.api;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;

//1.5
public class Updater {

	private String version;
	private String UPDATE_URL = "https://plugins.blackgriefing.com/?updatePl=PremiumLogin&t=b";
	
	public Updater(PremiumLoginMain main) {
		version = main.currentVersion();
	}
	
	public boolean isAvaliable() {
		if(HTTPClient.getValue(UPDATE_URL, "ver").contains("ERR")) {
			PremiumLoginMain.i().logConsole("ERR - Unable to check for updates.");
			return false;
		}
		if(HTTPClient.getValue(UPDATE_URL, "ver").equalsIgnoreCase(version)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String next() {
		String e = HTTPClient.getValue(UPDATE_URL, "ver");
		if(e.contains("ERR")) {
			return "ERROR_UNABLE";
		} 
		return e;
	}
	
}