package it.notreference.spigot.premiumlogin.auth;

import org.bukkit.entity.Player;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */

public class SpigotKey {

    private final String name;
    private final AuthPlugin authPlugin;
    private final Player player;
    private final byte[] verifyToken;

    public SpigotKey(String name, AuthPlugin authPlugin, Player player, byte[] verifyToken) {
        this.name = name;
        this.authPlugin = authPlugin;
        this.player = player;
        this.verifyToken = verifyToken;
    }

    /**
     * Returns the auth plugin.
     *
     * @return
     */
    public AuthPlugin getAuthPlugin() {
        return authPlugin;
    }

    /**
     *
     * Returns the verify token.
     *
     * @return
     */
    public byte[] getVerifyToken() {
        return verifyToken;
    }

    /**
     *
     * Returns the player.
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * Returns the player name.
     *
     * @return
     */
    public String getPlayerName() {
        return name;
    }

}
