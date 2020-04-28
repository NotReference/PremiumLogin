package it.notreference.spigot.premiumlogin.auth.misc;

import it.notreference.spigot.premiumlogin.auth.SpigotKey;
import org.bukkit.entity.Player;
import it.notreference.spigot.premiumlogin.auth.AuthPlugin;

public class SpigotKeyBuilder {

    private String name;
    private AuthPlugin authPlugin;
    private Player player;
    private byte[] verifyToken;

    public SpigotKeyBuilder() {
    }

    /**
     *
     * Sets the player name.
     *
     * @param name
     * @return
     */
    public SpigotKeyBuilder setPlayerName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * Sets the verify token.
     *
     * @param token
     * @return
     */
    public SpigotKeyBuilder setVerifyToken(byte [] token) {
        this.verifyToken = token;
        return this;
    }

    /**
     *
     * Sets the player.
     *
     * @param p
     * @return
     */
    public SpigotKeyBuilder setPlayer(Player p) {
        this.player = p;
        return this;
    }

    /**
     *
     * Sets the auth plugin.
     *
     * @param pl
     * @return
     */
    public SpigotKeyBuilder setAuthPlugin(AuthPlugin pl) {
        this.authPlugin = authPlugin;
        return this;
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

    public SpigotKey build() {
        return new SpigotKey(name, authPlugin, player, verifyToken);
    }

}
