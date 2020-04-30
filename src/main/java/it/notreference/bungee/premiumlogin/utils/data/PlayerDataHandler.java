package it.notreference.bungee.premiumlogin.utils.data;

import it.notreference.bungee.premiumlogin.utils.data.PlayerData;



/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public  interface PlayerDataHandler {

    /**
     *
     * Returns the player data of specifed username
     *
     * @param playerName
     * @return
     */
    PlayerData loadData(String playerName);

    /**
     *
     * Returns the player data of the specifed user id
     *
     * @param playerId
     * @return
     */
    PlayerData loadData(long playerId);

    /**
     *
     * Saves all players datas.
     *
     * @return
     */
    boolean saveAllDatas();

    /**
     *
     * Sets a data value.
     *
     * @param data
     * @param valueType
     * @param value
     */
    void setDataValue(PlayerData data, ValueType valueType, String value);

    /**
     *
     * Reloads all datas.
     *
     * @return
     */
    boolean reloadAllData();

    /**
     *
     * Loads all players datas
     *
     * @return
     */
    boolean init();


    /**
     *
     * Removes all datas with username equals name
     *
     * @param name
     */
    void removeDataWhere(String name);

    /**
     *
     * Removes data with specified id.
     *
     * @param id
     */
    void removeDataWhere(long id);

    /**
     *
     * Creates new profile data if not exists
     *
     */
    void createIfNotExists();

    /**
     *
     * Returns if data contains specified name exists.
     *
     * @param name
     * @return
     */
    boolean exists(String name);

    /**
     *
     * Returns if data contains specified id exists.
     *
     * @param id
     * @return
     */
    boolean exists(long id);

    /**
     *
     * Checks if data is corrupted.
     *
     * @param id
     * @return
     */
    boolean scanData(long id);

    /**
     *
     * Checks if data is corrupted.
     *
     * @param name
     * @return
     */
    boolean scanData(String name);
}
