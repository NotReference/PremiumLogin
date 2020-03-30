package it.notreference.bungee.premiumlogin.authentication;



import io.github.karmaconfigs.Bungee.API.PlayerAPI;
import it.notreference.bungee.premiumlogin.EventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.events.PremiumAutologinEvent;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.PlaceholderConf;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * 
 * PremiumLogin 1.6 By NotReference
 * 
 * @author NotReference
 * @version 1.6
 * @destination BungeeCord
 *
 */

/**
 * 
 * 
 * @since 1.0
 *
 */

public class AuthenticationHandler  {
	
	/**
	 * 
	 * Authenticate a Premium Player.
	 * 
	 * @param player
	 * @param auth-key
	 * @return response code. (integer)
	 */
	public static int login(ProxiedPlayer p, AuthenticationKey key) {
		
		
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
        	 Messages.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
			EventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
			return 1;
			} catch(Exception exc) {
				Messages.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("unable"));
				Messages.logConsole("Unable to premium login " + p.getName() + " with LockLogin API Support.");
				exc.printStackTrace();
				return 6;
			}
		}
		
		Messages.logConsole("[check] no locklogin found. Using AuthMe API..");
		
		
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
			EventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
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
