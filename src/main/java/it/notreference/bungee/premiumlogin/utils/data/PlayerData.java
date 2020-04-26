package it.notreference.bungee.premiumlogin.utils.data;


/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public interface PlayerData {


    /**
     *
     * Saves the data to the config.
     *
     */
    void saveData();

    /**
     *
     * Clears player data and remove it from config.
     *
     */
    void clearData();

    /**
     *
     * Returns the player name
     *
     * @return
     */
    String getPlayerName();

    /**
     *
     * Returns if this player is premium
     *
     * @return
     */
    boolean isPremium();

    /**
     *
     *
     * Returns the last used UUID on the server.
     *
     */
    String getLastUsedUUID();

    /**
     *
     * Returns the player UUID.
     *
     * @return
     */
    long getPlayerId();

    /**
     *
     * Returns if is registred.
     *
     * @param plugin The plugin (example AuthMe or LockLogin)
     * @return
     */
    boolean isRegistered(String plugin);



}
