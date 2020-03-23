package it.notreference.bungee.premiumlogin.api;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import it.notreference.bungee.premiumlogin.utils.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.AuthType;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;

/**
 * PremiumLogin 1.2 by NotReference
 *
 * @interface LoginHandler
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public interface LoginHandler {

    /**
     * Login premium players.
     *
     * @param do forcelogin.
     * @return 
     */
	LoginResponse simpleLogin(ProxiedPlayer p, AuthenticationKey k);
	
    /**
     * Build a simple authentication key.
     *
     * @return authenticationkey easy builder.
     */
	AuthenticationKey buildSimpleKey(ProxiedPlayer p, ServerInfo info, TipoConnessione con, AuthType type);
	
    /**
     * Get the no-legacy config value.
     *
     * @return if old legacy premium connections are allowed.
     */
	boolean isLegacyAllowed();
	
    /**
     * Get the allow-online config value.
     *
     * @return if the online mod is allowed.
     */
	boolean isOnlineModeAllowed();
	
    /**
     * Get the authentication server.
     *
     * @return auth-server
     */
     String getAuthServer();
     
     /**
      * Get the all-server-are-server-auth config value.
      *
      * @return if all servers are auth server.
      */
     boolean allAreAuthServers();
	
}
