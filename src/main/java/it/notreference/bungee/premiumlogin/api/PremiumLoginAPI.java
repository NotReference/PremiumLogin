package it.notreference.bungee.premiumlogin.api;

import it.notreference.bungee.premiumlogin.api.events.UUIDSetupEvent;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
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

public class PremiumLoginAPI implements LoginHandler{


	@Override
	public LoginResponse simpleLogin(ProxiedPlayer p, AuthenticationKey k) {
		
		if(AuthenticationHandler.login(p, k) == 1) {
			return LoginResponse.SUCCESS;
		} else if(AuthenticationHandler.login(p, k) == 2) {
			return LoginResponse.NOPREMIUM;
		} else if(AuthenticationHandler.login(p, k) == 3) {
			return LoginResponse.NOTONLINE;
		} else if(AuthenticationHandler.login(p, k) == 4) {
			return LoginResponse.NEEDSTOLOGINWITHLAUNCHER;
		} else if(AuthenticationHandler.login(p, k) == 5) {
			return LoginResponse.ERROR;
		} else if(AuthenticationHandler.login(p, k) == 6) {
			return LoginResponse.ERROR;
		} else if(AuthenticationHandler.login(p, k) == 7) {
			return LoginResponse.NOLEGACY;
		} else if(AuthenticationHandler.login(p, k) == 0) {
			return LoginResponse.ERROR;
		} else {
			return LoginResponse.UNABLE;
		}
		
	}

	/**
	 *
	 * Returns the player of the UUIDSetupEvent
	 *
	 * @param event
	 * @return null if fail or the player if success
	 */
	public ProxiedPlayer getPlayerFrom(UUIDSetupEvent event) {
		try {

			return ProxyServer.getInstance().getPlayer(event.getPlayerConnection().getName());

		} catch(Exception exc) {
			return null;
		}
	}

	@Override
	public AuthenticationKey buildSimpleKey(ProxiedPlayer p, ServerInfo info, TipoConnessione con) {
	
		return new AuthenticationBuilder()
		              .setAuthType(AuthType.AUTO)
		              .setConnectionType(con)
		              .setPlayer(p)
		              .setUUID(p.getUniqueId())
		              .setServer(info)
		              .setName(p.getName())
		              .build();
	}

	@Override
	public boolean isLegacyAllowed() {
		return ConfigUtils.allowLegacy();
	}

	@Override
	@Deprecated
	public boolean isOnlineModeAllowed() {
	
		return false;
	}

	@Override
	public String getAuthServer() {
		return ConfigUtils.getConfStr("auth-server");
	}

	@Override
	public boolean allAreAuthServers() {
		return ConfigUtils.getConfBool("all-servers-are-server-auth");
	}

	@Deprecated
	public void enablePremium(String name) {
		ConfigUtils.enablePremium(name);
	}

	@Deprecated
	public void disablePremium(String name) {
		ConfigUtils.disablePremium(name);
	}
	
	public boolean hasPremiumLogin(String name) {
		return ConfigUtils.hasPremiumAutoLogin(name);
	}
	
	
	
	
}
