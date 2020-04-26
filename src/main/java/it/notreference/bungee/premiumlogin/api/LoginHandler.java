package it.notreference.bungee.premiumlogin.api;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.ConnectionType;



/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
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
	AuthenticationKey buildSimpleKey(ProxiedPlayer p, ServerInfo info, ConnectionType con);
	
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
     * @deprecated always false
     */
    @Deprecated
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
