package it.notreference.bungee.premiumlogin.api;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;

/**
 *
 * PremiumLogin 1.6.2 By NotReference
 *
 * @author NotReference
 * @version 1.6.2
 * @destination BungeeCord
 *
 */

public interface LoginHandler {

    /**
     * Login premium players.
     *
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
