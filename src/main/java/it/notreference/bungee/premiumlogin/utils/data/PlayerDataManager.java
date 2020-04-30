package it.notreference.bungee.premiumlogin.utils.data;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.data.PlayerDataHandler;
import it.notreference.bungee.premiumlogin.utils.files.DataLoader;



/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class PlayerDataManager implements PlayerDataHandler {


    private PremiumLoginMain main;
    private DataLoader dataLoader;

    public PlayerDataManager(PremiumLoginMain mainPlugin, DataLoader dataLoader) {
        this.main = mainPlugin;
    }


    @Override
    public PlayerData loadData(String playerName) {
        return null;
    }

    @Override
    public PlayerData loadData(long playerId) {
        return null;
    }

    @Override
    public boolean saveAllDatas() {
        return false;
    }

    @Override
    public void setDataValue(PlayerData data, ValueType valueType, String value) {

    }

    @Override
    public boolean reloadAllData() {
        return false;
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void removeDataWhere(String name) {

    }

    @Override
    public void removeDataWhere(long id) {

    }

    @Override
    public void createIfNotExists() {

    }

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public boolean exists(long id) {
        return false;
    }

    @Override
    public boolean scanData(long id) {
        return false;
    }

    @Override
    public boolean scanData(String name) {
        return false;
    }
}
