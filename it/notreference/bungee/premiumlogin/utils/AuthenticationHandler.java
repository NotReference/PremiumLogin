package it.notreference.bungee.premiumlogin.utils;



import io.github.karmaconfigs.Bungee.API.PlayerAPI;
import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.events.PremiumAutologinEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * PremiumLogin 1.2 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class AuthenticationHandler  {

	
	//Codes:
	//0 - UUID Error (Not showed anymore).
	//1 - Success
	//2 - No Premium
	//3 - Not Online
	//4 - No Premium Connection but is premium.
	//5 - Arleady logged in. (Not showed anymore).
	//6 - Other error..
	//7 - User logged in legacy mode && non consentito nel config.
	
	//3 + 6 (9) = combo of Not Online // Other error.
	
	
	public static int login(ProxiedPlayer p, AuthenticationKey key) {
		
		//if(isPremiumLogged(p)) {
			//Messages.logConsole("arleady_logged_user: " + p.getName());
			//return 5;
		//}
		
		//UUID Check 1
		//String q = p.getUniqueId().toString().substring(0, 4);
		//String qk = key.getUUID().toString().substring(0, 4);
		//if(!q.equalsIgnoreCase(qk)) {
			//UUID Error
			//return 0;
		//}
		
		
		if(!ConfigUtils.hasPremiumAutoLogin(p)) {
			return 6;
		}
		
		if(!UUIDUtils.isPremium(p)) {
			Messages.logConsole("[err] nopremium_user: " + p.getName());
			return 2;
		}
		
		if(key.getConType() == TipoConnessione.LEGACY) {
		
			if(!ConfigUtils.allowLegacy()) {
				Messages.logConsole("[err=>[nolegacy]] connectionblock_user: " + p.getName());
				return 7;
			}
		if(!UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
			Messages.logConsole("[err] no_launcher_user: " + p.getName());
			return 4;
		}
		} else {
			
			if(!UUIDUtils.isPremiumConnection(p)) {
				Messages.logConsole("[err] no_launcher_user: " + p.getName());
				return 4;
			}
		}
		
		if(!p.isConnected()) {
			return 3;
		}
		
		
		//1.4 - Check locklogin
		Messages.logConsole("[check] checking locklogin..");
		if(PremiumLoginMain.i().isHooked("LockLogin")) {
			Messages.logConsole("[locklogin] hooked into locklogin.");
			Messages.logConsole("[action] forcelogging (user= " + p.getName() + ")");
			try {
			PlayerAPI api = new PlayerAPI(PremiumLoginMain.i().getLockLogin(), p);
			api.setLogged(true, ConfigUtils.getConfStr("lock-login"));
			Messages.sendParseColors(p, ConfigUtils.getConfStr("auto-login-premium"));
			Messages.logConsole("[[premium:forcelogin] donelogin_user: " + p.getName());
			 Messages.logConsole(p.getName() + " has been forcelogged (premium mode)."); 
        	 Messages.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("auto-login-premium"));
        	 Messages.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
			PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
			return 1;
			} catch(Exception exc) {
				Messages.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("unable"));
				Messages.logConsole("Unable to premium login " + p.getName());
				exc.printStackTrace();
				return 6;
			}
		}
		//Forcelogghiamo.
		
		
		
		Messages.logConsole("[action] forcelogging (user= " + p.getName() + ")");
		try {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
         out.writeUTF("AuthMe.v2");
         out.writeUTF("perform.login");
         out.writeUTF(p.getName());
         key.playerServer().sendData("BungeeCord", out.toByteArray());
         PremiumLoginMain.i().getProxy().getServerInfo(ConfigUtils.getConfStr("auth-server")).sendData("BungeeCord", out.toByteArray());
         Messages.logConsole("[[premium:forcelogin] donelogin_user: " + p.getName());
         if(key.getAuthType() == AuthType.AUTO) {
        	 Messages.logConsole(p.getName() + " has been forcelogged (premium mode)."); 
        	 Messages.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("auto-login-premium"));
        	 Messages.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
			PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));

        	 // getLogged().add(p);
         } 
         return 1;
		} catch(Exception exc) {
			Messages.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("unable"));
			Messages.logConsole("Unable to premium login " + p.getName());
			exc.printStackTrace();
			return 6;
		}
	}

	
	
	
}
