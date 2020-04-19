package it.notreference.bungee.premiumlogin.api;

import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 *
 * PremiumLogin 1.6.2 By NotReference
 *
 * @author NotReference
 * @version 1.6.2
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

	@Override
	public AuthenticationKey buildSimpleKey(ProxiedPlayer p, ServerInfo info,TipoConnessione con, AuthType type) {
	
		return new AuthenticationBuilder()
		              .setAuthType(type)
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
	public boolean isOnlineModeAllowed() {
	
		return ConfigUtils.getConfBool("allow-online");
	}

	@Override
	public String getAuthServer() {
		return ConfigUtils.getConfStr("auth-server");
	}

	@Override
	public boolean allAreAuthServers() {
		return ConfigUtils.getConfBool("all-servers-are-server-auth");
	}
	
	public void enablePremium(String name) {
		ConfigUtils.enablePremium(name);
	}
	
	public void disablePremium(String name) {
		ConfigUtils.disablePremium(name);
	}
	
	public boolean hasPremiumLogin(String name) {
		return ConfigUtils.hasPremiumAutoLogin(name);
	}
	
	
	
	
}
