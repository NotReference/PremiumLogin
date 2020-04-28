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
public interface SpigotAuthenticationHandler {

    /**
     *
     * Authenticates a premium player
     *
     * @param spigotPlayer
     * @param key
     */
    void premiumLogin(Player spigotPlayer, SpigotKey key);

    /**
     *
     * Prints an info message.
     *
     * @param msg
     */
    void info(String msg);

    /**
     *
     * Prints an error message.
     *
     * @param msg
     */
    void error(String msg);

    /**
     *
     * Returns the default auth plugin.
     *
     * @return
     */
    AuthPlugin getDefaultAuthPlugin();

    /**
     *
     * Generates a random password.
     *
     * @return
     */
    String generateRandomSecurePassword();



}
